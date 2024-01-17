(ns clj-aoc-2023.day07-test
  (:require [clj-aoc-2023.day07 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.test :refer :all]))

(def test-input
  (lines-with-indent-trimmed "
      32T3K 765
      T55J5 684
      KK677 28
      KTJJT 220
      QQQJA 483"))

(def real-input (read-real-input "day07"))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 6440 (sut/part1 test-input))))
  (testing "Part 1 with real input"
    (is (= 253954294 (sut/part1 real-input)))))

(deftest sorting-hands
  (testing "Sorting" 
    (is (= ["32T3K" "KTJJT" "KK677" "T55J5" "QQQJA"]
         (sort-by sut/hand-value
               ["32T3K" "T55J5" "KK677" "KTJJT" "QQQJA"])))))

(comment
  (deftest part-2
   (testing "Part 2 with test input"
     (is (= -1 (sut/part2 test-input))))
   (testing "Part 2 with real input"
     (is (= -1 (sut/part2 real-input))))))

(deftest test-hand-type
  (testing "hand-type figures out the types of hands" 
    (is (= :five-of-a-kind  (sut/hand-type "AAAAA")))
    (is (= :four-of-a-kind  (sut/hand-type "AA8AA")))
    (is (= :full-house      (sut/hand-type "23332")))
    (is (= :three-of-a-kind (sut/hand-type "TTT98"))) 
    (is (= :two-pair        (sut/hand-type "23432")))
    (is (= :one-pair        (sut/hand-type "A23A4")))))
