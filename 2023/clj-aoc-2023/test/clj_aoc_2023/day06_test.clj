(ns clj-aoc-2023.day06-test
  (:require [clj-aoc-2023.day06 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.test :refer :all]))

(def test-input
  (lines-with-indent-trimmed "
      Time:      7  15   30
      Distance:  9  40  200"))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 288 (sut/part1 test-input))))
  (testing "Part 1 with real input"
    (is (= 227850 (sut/part1 (read-real-input "day06"))))))

(deftest part-2
  (testing "Part 2 with test input"
    (is (= 71503 (sut/part2 test-input))))
  (testing "Part 2 with real input"
    (is (= 42948149 (sut/part2 (read-real-input "day06"))))))
