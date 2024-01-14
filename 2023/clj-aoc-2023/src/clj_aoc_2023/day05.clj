(ns clj-aoc-2023.day05
  (:require [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]))

(defn- parse-range [line]
  (let [[_ dest-start-str src-start-str length-str] (re-matches #"^(\d+) (\d+) (\d+)$" line)]
    {:dest-start (parse-long dest-start-str)
     :src-start (parse-long src-start-str)
     :length (parse-long length-str)}))

(defn- parse-map [section]
  (let [[_ source destination] (re-matches #"^([a-z]+)-to-([a-z]+) map:$"
                                           (first section))
        ranges (sort-by :src-start (map parse-range (rest section)))]
    {:source source
     :destination destination
     :ranges ranges}))

(defn maybe-range-in-gap [[range1 range2]]
  (let [end1 (+ (:src-start range1) (:length range1))]
    (when (< end1 (:src-start range2))
      (let [length (- (:src-start range2) end1)]
        {:src-start end1
         :dest-start end1
         :length length}))))

(defn interior-ranges [ranges]
  (keep maybe-range-in-gap (partition 2 1 ranges)))

(defn augment-with-additional-first-range [ranges]
  (if (> (:src-start (first ranges)) 0)
    (conj ranges
          {:dest-start 0
           :src-start 0
           :length (:src-start (first ranges))})
    ranges))

(defn with-additiona-last-rnage [ranges]
  (let [last-range (+ (:src-start (last ranges)) (:length (last ranges)))]
    (conj ranges
          {:dest-start last-range
           :src-start last-range
           :length 100000000000})))

(defn- augment-map [this-map]
  (let [ranges (:ranges this-map)
        ranges-in-gaps (interior-ranges ranges)
        ranges-with-gaps-filled (sort-by :src-start
                                         (concat ranges ranges-in-gaps))
        maybe-with-additional-first (augment-with-additional-first-range ranges-with-gaps-filled)
        with-additional-last (with-additiona-last-rnage maybe-with-additional-first)
        ]
    (assoc this-map :ranges with-additional-last)))

(defn parse-seeds [line]
  (let [[_ ston] (re-matches #"^seeds: (.+)$" (first line))
        seeds-strs (s/split ston #" ")
        seeds (map parse-long seeds-strs)]
    seeds))

(defn parse-input [input]
  (let [sections (split-by-p empty? input)
        initial-seeds (->> sections
                           (first)
                           (parse-seeds))
        maps (map parse-map (drop 1 sections))]
    [initial-seeds maps]))

(defn- apply-range-to [range thing]
  (let [{src-start :src-start
         dest-start :dest-start
         length :length} range
        pos-within-range (- thing src-start)]
    (if (and
         (>= pos-within-range 0)
         (< pos-within-range length))
      (+ pos-within-range dest-start)
      nil)))

(defn overlap-with [[start1 length1] [start2 length2]]
  (let [end1 (+ start1 length1)
        end2 (+ start2 length2)
        start (max start1 start2)
        end (min end1 end2)]
    (if (<= end start)
      nil
      [start (- end start)])))

(defn apply-range-to-seed-range [map-range seed-range]
  (let [{start1 :src-start
         length1 :length} map-range
        [start2 length2] seed-range])

  (let [{start1 :src-start
         length1 :length} map-range
        [start2 length2] seed-range
        overlap (overlap-with [start1 length1] [start2 length2])]


    (if (some? overlap)
      (let [[start length] overlap
            offset (- start start1)]

        ;; (println "this" map-range)
        ;; (println "overlap" overlap)
        ;; (println "offset me do" offset)


        [[(+ (:dest-start map-range) offset) (second overlap)]])
      [])))

(defn apply-all-ranges-to-seed-range [mapping seed-range]
  (apply concat
         (filter not-empty
                 (map #(apply-range-to-seed-range % seed-range) (:ranges mapping)))))

(defn apply-all-ranges-to-seed-ranges [mapping seed-ranges]
  (mapcat #(apply-all-ranges-to-seed-range mapping %) seed-ranges))

(defn apply-map-to [this-map thing]
  (let [{source :source
         destination :destination
         ranges :ranges} this-map]
    (or (first (keep #(apply-range-to % thing) ranges))
        thing)))

(defn map-all-the-way [maps seed]
  (reduce #(apply-map-to %2 %1) seed maps))

(defn map-ranges-all-the-freaking-way [maps seed-ranges]
  (reduce #(apply-all-ranges-to-seed-ranges %2 %1) seed-ranges maps))

(defn part1 [input]
  (let [[seeds maps-unaugmented] (parse-input input)
        maps (map augment-map maps-unaugmented)]
    (apply min
           (map #(map-all-the-way maps %) seeds))))

(defn part2 [input]
  (let [[seeds-list maps-unaugmented] (parse-input input)
        maps (map augment-map maps-unaugmented)
        seed-ranges (partition 2 seeds-list)
        mapped (map-ranges-all-the-freaking-way maps seed-ranges)]
    (apply min (map first mapped))))

(comment
  (use 'clojure.pprint)

  (part1 test-input)
  (part2 test-input)

  ;; (pprint
  ;;  (map-ranges-all-the-freaking-way maps [[82 1]]))

  (apply-all-ranges-to-seed-ranges (first maps) [[82 1]])
  (apply-all-ranges-to-seed-range (first maps) [82 1])

  (let [[seeds-list maps-unaugmented] (parse-input real-input)
        ;; maps (map augment-map maps-unaugmented)
        ]

    (println "original")
    (pprint (:ranges (first maps-unaugmented)))

    (println "interior shizzle")
    (pprint
     (interior-ranges
      (sort-by :src-start (:ranges (first maps-unaugmented))))))
  (let [[seeds-list maps-unaugmented] (parse-input real-input)
        maps (map augment-map maps-unaugmented)
        seed-ranges (partition 2 seeds-list)]
    (println (first maps))

    (println
     (apply-all-ranges-to-seed-range (second maps) [1165760326 149945844]))
    (println
     (apply-all-ranges-to-seed-range (second maps) [138187491 22674358]))
    (pprint (sort-by :src-start (:ranges (second maps)))))

  ;; input range: [4045092792 172620202]
  ;; 1 mapped range: [[1165760326 149945844] [138187491 22674358]]
  ;; 2 mapped range: [[138187491 22674358]]

  (def real-input (read-real-input "day05"))

  (def maps (let [[seeds-list maps-unaugmented] (parse-input real-input)
                  maps-ston (map augment-map maps-unaugmented)
                  seed-ranges (partition 2 seeds-list)]
              maps-ston))

  (apply-all-ranges-to-seed-range (first maps) [2031777983, 63513119])

  (apply-map-to fertilizer-to-water 53)

  (split-by-p empty? test-input)

  (partition 2 1 [:a :b :c :d :e :f])

  (def test-input
    (lines-with-indent-trimmed "
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4")))

