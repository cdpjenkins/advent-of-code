(ns clj-aoc-2023.day04-test
  (:require [clj-aoc-2023.day04 :as sut]
            [clojure.string :as s]
            [clojure.test :refer :all]))

(def day04-test-input
  (s/split-lines
"Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"))

(defn read-real-input [name]
  (s/split-lines
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))

(deftest part-1
  (testing "Part 1 with test input"
    (is (= 13 (sut/day04-part1 day04-test-input))))
  (testing "Part 1 with real input"
    (is (= 18653 (sut/day04-part1 (read-real-input "day04"))))))

(deftest part-2
  (testing "Part 2 with test input"
    (is (= 30 (sut/day04-part2 day04-test-input))))
  (testing "Part 2 with real input"
    (is (= 5921508 (sut/day04-part2 (read-real-input "day04")))))

  (comment))

(comment
  (sut/day04-part2 day04-test-input)


  (use 'clojure.pprint)

  (pprint
   (let [matcher (re-matcher #"\d+" "....123...456...")]
     (loop [nums #{}]
       (if (.find matcher)
         (do
           (recur (conj nums {:begin (.start matcher)
                              :end (.end matcher)
                              :num (parse-long (.group matcher))})))
         nums)))))
