(ns clj-aoc-2023.day05-test
  (:require [clj-aoc-2023.day05 :as sut]
            [clj-aoc-2023.util :refer :all]
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

(deftest part-2
  (testing "Part 2 with test input"
    (is (= 46 (sut/part2 test-input))))
  (testing "Part 2 with real input"
    (is (= 1928058 (sut/part2 (read-real-input "day05"))))))

(deftest mapping-troublesome-ranges
  (testing "This one is very troublesome"
    (is (= [ [357106588 3453932]]
           (sut/apply-range-to-seed-range
            {:dest-start 357106588
             :src-start 2091837170
             :length 132629216} 
            [2031777983 63513119]))) 
    (is (= [ [1990128498 40628127]]
           (sut/apply-range-to-seed-range
            {:dest-start 1315706170
             :src-start 1357355655
             :length 734481515}
            [2031777983 40628127]))) 
    (is (= '([357106588 3453932] [1990128498 60059187]) 
           (sut/apply-all-ranges-to-seed-range
            {:ranges [{:dest-start 357106588
                       :src-start 2091837170
                       :length 132629216}
                      {:dest-start 1315706170
                       :src-start 1357355655
                       :length 734481515}]}
            [2031777983, 63513119])))))

(deftest test-finding-overlap
  (testing "Can find overlap of two ranges"
    (is (= nil (sut/overlap-with [0 5] [10 5])))
    (is (= nil (sut/overlap-with [10 5] [0 5])))
    (is (= [2 3] (sut/overlap-with [0 5] [2 5])))))

(deftest test-mapping-ranges
  (testing "Can map seed ranges over map ranges"
    (let [a-range {:dest-start 110
                   :src-start 10
                   :length 5}]
      (is (= [[110 5]] (sut/apply-range-to-seed-range a-range [5 10])))
      (is (= [[112 3]] (sut/apply-range-to-seed-range a-range [12 20]))))))
