(ns clj-aoc-2023.day08-test
  (:require [clj-aoc-2023.day08 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.test :refer :all]))

(def test-input1
  (lines-with-indent-trimmed "
      RL

      AAA = (BBB, CCC)
      BBB = (DDD, EEE)
      CCC = (ZZZ, GGG)
      DDD = (DDD, DDD)
      EEE = (EEE, EEE)
      GGG = (GGG, GGG)
      ZZZ = (ZZZ, ZZZ)"))

(def test-input2
  (lines-with-indent-trimmed "
      LR

      11A = (11B, XXX)
      11B = (XXX, 11Z)
      11Z = (11B, XXX)
      22A = (22B, XXX)
      22B = (22C, 22C)
      22C = (22Z, 22Z)
      22Z = (22B, 22B)
      XXX = (XXX, XXX)"))

(def real-input (read-real-input "day08"))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 2 (sut/part1 test-input1)))) 
  (testing "Part 1 with real input"
    (is (= 20777 (sut/part1 real-input)))))


 (deftest part-2
   (testing "Part 2 with test input"
     (is (= 6 (sut/part2 test-input2))))
   (testing "Part 2 with real input"
     (is (= 13289612809129 (sut/part2 real-input)))))

(deftest test-move 
  (testing "Moves work sanely" 
    (let [network (sut/network-of test-input1)] 
      (is (= "BBB" (sut/move network \L "AAA")))
      (is (= "CCC" (sut/move network \R "AAA"))))))