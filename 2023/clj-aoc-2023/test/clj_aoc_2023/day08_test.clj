(ns clj-aoc-2023.day08-test
  (:require [clj-aoc-2023.day08 :as sut]
            [clj-aoc-2023.util :refer :all]
            [clojure.test :refer :all]))

(def test-input
  (lines-with-indent-trimmed "
      RL

      AAA = (BBB, CCC)
      BBB = (DDD, EEE)
      CCC = (ZZZ, GGG)
      DDD = (DDD, DDD)
      EEE = (EEE, EEE)
      GGG = (GGG, GGG)
      ZZZ = (ZZZ, ZZZ)"))

(def real-input (read-real-input "day08"))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 2 (sut/part1 test-input)))) 
  (testing "Part 1 with real input"
    (is (= 20777 (sut/part1 real-input)))))

(comment
 (deftest part-2
   (testing "Part 2 with test input"
     (is (= -1 (sut/part2 test-input))))
   (testing "Part 2 with real input"
     (is (= -1 (sut/part2 real-input)))))
 )

(deftest test-move 
  (testing "Moves work sanely" 
    (let [network (sut/network-of test-input)] 
      (is (= "BBB" (sut/move network \L "AAA")))
      (is (= "CCC" (sut/move network \R "AAA"))))))