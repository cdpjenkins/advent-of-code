(ns clj-aoc-2023.day01-test
  (:require [clj-aoc-2023.day01 :as sut]
            [clj-aoc-2023.util :refer [read-real-input]]
            [clojure.test :refer :all]))

(def day01-test-input ["1abc2"
                       "pqr3stu8vwx"
                       "a1b2c3d4e5f"
                       "treb7uchet"])

(def day01-test-input-2 ["two1nine"
                         "eightwothree"
                         "abcone2threexyz"
                         "xtwone3four"
                         "4nineeightseven2"
                         "zoneight234"
                         "7pqrstsixteen"])

(deftest part-1
  (testing "Day 01 part 1 with test input"
    (is (= 142 (sut/day01-part1 day01-test-input) )))
  (testing "Day 01 part 1 with real input"
    (is (= 54951 (sut/day01-part1 (read-real-input "day01")) ))))

(deftest part-2
  (testing "Day 01 part 2 with test input"
    (is (= 281 (sut/day01-part2 day01-test-input-2)))) 
  (testing "Day 01 part 2 with real input"
    (is (= 55218 (sut/day01-part2 (read-real-input "day01"))))))
