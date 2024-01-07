(ns clj-aoc-2023.day01-test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clj-aoc-2023.day01 :as sut]))

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

(defn read-real-input [name]
  (s/split-lines 
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))


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
