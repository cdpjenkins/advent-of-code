(ns clj-aoc-2023.day03-test
  (:require [clj-aoc-2023.day03 :as sut]
            [clojure.string :as s]
            [clojure.test :refer :all]))

(def day03-test-input
  ["467..114.."
   "...*......"
   "..35..633."
   "......#..."
   "617*......"
   ".....+.58."
   "..592....."
   "......755."
   "...$.*...."
   ".664.598.."])

(defn read-real-input [name]
  (s/split-lines
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))

(deftest part-1
  (testing "Day 01 part 1 with test input"
    (is (= 4361 (sut/day03-part1 day03-test-input))))
  (testing "Day 01 part 1 with real input"
    (is (= 546312 (sut/day03-part1 (read-real-input "day03"))))))

(deftest part-2
  (comment
    (testing "Part 2 with test input"
      (is (= 2286 (sut/day03-part2 day03-test-input))))
    (testing "Part 2 with real input"
      (is (= 63711 (sut/day03-part2 (read-real-input "day03")))))))

(deftest internals

  (testing "Adjacency between symbols and part-numbers"
    (let [number {:begin-x 1
                  :end-x 3
                  :y 5,
                  :num 123}]
      (are [sym] (is (sut/adjacent-to-symbol number sym))
        [0 4]
        [1 4]
        [2 4]
        [3 4]
        [0 5] 
        [0 6]
        [1 6]
        [2 6]
        [3 6])

      (are [sym] (is (not (sut/adjacent-to-symbol number sym)))
        [0 0]
        [-1 5]
        [12 5]
        [10 10]))))

(comment
  (def m
    (re-matcher #"\d+" "....123...456..."))

  (re-seq)


  m

  (.find m)

  m

  (.start m)
  (.end m)


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
