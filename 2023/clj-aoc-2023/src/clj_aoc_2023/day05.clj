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

(defn- maybe-range-in-gap [[range1 range2]]
  (let [end1 (+ (:src-start range1) (:length range1))]
    (when (< end1 (:src-start range2))
      (let [length (- (:src-start range2) end1)]
        {:src-start end1
         :dest-start end1
         :length length}))))

(defn- with-additional-interior-ranges [ranges]
  (concat ranges
          (keep maybe-range-in-gap (partition 2 1 ranges))))

(defn- with-additional-first-range [ranges]
  (if (> (:src-start (first ranges)) 0)
    (conj ranges
          {:dest-start 0
           :src-start 0
           :length (:src-start (first ranges))})
    ranges))

(defn- with-additiona-last-range [ranges]
  (let [last-range (+ (:src-start (last ranges)) (:length (last ranges)))]
    (conj ranges
          {:dest-start last-range
           :src-start last-range
           :length 100000000000})))

(defn- augment-map [this-map] 
  (assoc this-map :ranges
         (->> this-map
              (:ranges)
              (with-additional-interior-ranges)
              (sort-by :src-start)
              (with-additional-first-range)
              (with-additiona-last-range)
              (sort-by :src-start))))

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

(defn- apply-range-to-single-value [range thing]
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
        [start2 length2] seed-range
        overlap (overlap-with [start1 length1] [start2 length2])]
    (if (some? overlap)
      (let [[start length] overlap
            offset (- start start1)]
        [[(+ (:dest-start map-range) offset) (second overlap)]])
      [])))

(defn apply-all-ranges-to-seed-range [mapping seed-range]
  (apply concat
         (filter not-empty
                 (map #(apply-range-to-seed-range % seed-range) (:ranges mapping)))))

(defn apply-all-ranges-to-seed-ranges [mapping seed-ranges]
  (mapcat #(apply-all-ranges-to-seed-range mapping %) seed-ranges))

(defn apply-map-to [this-map thing]
  (or (first (keep #(apply-range-to-single-value % thing) (:ranges this-map)))
      thing))

(defn map-single-values-all-the-way [maps seed]
  (reduce #(apply-map-to %2 %1) seed maps))

(defn map-ranges-all-the-way [maps seed-ranges]
  (reduce #(apply-all-ranges-to-seed-ranges %2 %1) seed-ranges maps))

(defn part1 [input]
  (let [[seeds maps-unaugmented] (parse-input input)
        maps (map augment-map maps-unaugmented)]
    (apply min
           (map #(map-single-values-all-the-way maps %) seeds))))

(defn part2 [input]
  (let [[seeds-list maps-unaugmented] (parse-input input)
        maps (map augment-map maps-unaugmented)
        seed-ranges (partition 2 seeds-list)
        mapped (map-ranges-all-the-way maps seed-ranges)]
    (apply min (map first mapped))))
