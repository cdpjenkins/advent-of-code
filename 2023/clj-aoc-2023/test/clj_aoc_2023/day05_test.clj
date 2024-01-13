(ns clj-aoc-2023.day05-test
  (:require [clj-aoc-2023.day05 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]
            [clojure.test :refer :all]))

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

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 35 (sut/part1 test-input))))
  (testing "Part 1 with real input"
    (is (= 165788812 (sut/part1 (read-real-input "day05"))))))

(comment
  (deftest part-2
    (testing "Part 2 with test input"
      (is (= 30 (sut/day04-part2 day04-test-input))))
    (testing "Part 2 with real input"
      (is (= 5921508 (sut/day04-part2 (read-real-input "day05"))))))
  )
