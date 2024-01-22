(ns clj-aoc-2023.day09-test
  (:require [clj-aoc-2023.day09 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.test :refer :all]))

(def test-input
  (lines-with-indent-trimmed "
      0 3 6 9 12 15
      1 3 6 10 15 21
      10 13 16 21 30 45"))

(def real-input (read-real-input "day09"))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 114 (sut/part1 test-input))))
  (testing "Part 1 with real input"
    (is (= 1637452029 (sut/part1 real-input)))))

(deftest part-2
  (testing "Part 2 with test input"
    (is (= 2 (sut/part2 test-input))))
  (testing "Part 2 with real input"
    (is (= 908 (sut/part2 real-input)))))

(deftest test-all-items-the-same
  (testing "Recognises when all items in a list are the same"
    (is (sut/all-items-the-same? [1 1 1 1 1 1 1 1 1])))
  (testing "Recognises when not all ite4ms in a list are the same"
    (is (not (sut/all-items-the-same? [1 2 3 4 5])))))

(deftest test-differences
  (testing "Can compute differences"
    (is (= [1 3 5 7 9] (sut/differences [0 1 4 9 16 25])))))

(deftest test-extrapolate-forwards
  (testing "Can extrapolate forwards if all items in list are the same"
    (is (= 1 (sut/extrapolate-forwards [1 1 1 1 1 1 1 1]))))

  (testing "Can extrapolate forwards if list has constant differences"
    (is (= 10 (sut/extrapolate-forwards [1 2 3 4 5 6 7 8 9]))))

  (testing "Can extrapolate forwards if constant second-order differences"
    (is (= 16 (sut/extrapolate-forwards [0 1 4 9])))))