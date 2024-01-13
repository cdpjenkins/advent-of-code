(ns clj-aoc-2023.day05 
  (:require [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]))

(defn- parse-range [line]
  (let [[_ dest-start-str src-start-str length-str] (re-matches #"^(\d+) (\d+) (\d+)$" line)] 
    {:dest-start (parse-long dest-start-str)
     :src-start (parse-long src-start-str)
     :length (parse-long length-str)}) 
  )

(defn- parse-map [section]
  (let [[_ source destination] (re-matches #"^([a-z]+)-to-([a-z]+) map:$"
                                           (first section))
        ranges (map parse-range (rest section))]
    {:source source
     :destination destination
     :ranges ranges}))

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

(defn apply-map-to [this-map thing]
  (let [{source :source
         destination :destination
         ranges :ranges} this-map]
    (or (first (keep #(apply-range-to % thing) ranges))
        thing)))

(defn map-all-the-way [maps seed]
   (reduce #(apply-map-to %2 %1) seed maps))

(defn part1 [input]
  (let [[seeds maps] (parse-input input)]
    (apply min
     (map #(map-all-the-way maps %) seeds))))
 
(defn part2 [input]
  1234)

(comment
  (use 'clojure.pprint)

  (part1 test-input)

  (def fertilizer-to-water
    {:source "fertilizer",
     :destination "water",
     :ranges
     '({:dest-start 49, :src-start 53, :length 8}
       {:dest-start 0, :src-start 11, :length 42}
       {:dest-start 42, :src-start 0, :length 7}
       {:dest-start 57, :src-start 7, :length 4})})

  (apply-map-to fertilizer-to-water 53)

  (split-by-p empty? test-input)

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
      56 93 4"))
  )

