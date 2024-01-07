(ns clj-aoc-2023.core-test
  (:require [clojure.test :refer :all]
            [clj-aoc-2023.core :refer :all]
            [clojure.string :as s]))

(defn read-real-input [name]
  (s/split-lines 
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))

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

(defn to-digit [it]
  (cond
    (re-matches #"\d" it) (Integer. it)
    (= "one" it) 1
    (= "two" it) 2
    (= "three" it) 3
    (= "four" it) 4
    (= "five" it) 5
    (= "six" it) 6
    (= "seven" it) 7
    (= "eight" it) 8
    (= "nine" it) 9))

(defn first-digit-or-word [line]
    (->> line
         (re-seq #"(?=([0-9]|one|two|three|four|five|six|seven|eight|nine))")
         (first)
         (second)
         (to-digit)))

(defn last-digit-or-word [line]
  (->> line
       (re-seq #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))")
       (last)
       (second)
       (to-digit)))

(defn day01-part2 [input]
    (->> input
        (map #(+
               (* 10 (first-digit-or-word %))
               (last-digit-or-word %)))
        (reduce + 0)))

(defn first-digit [line]
  (->> line
       (re-seq #"\d")
       (first)
       (Integer.)))

(defn last-digit [line]
  (->> line
       (re-seq #"\d")
       (last)
       (Integer.)))

(defn value-of-first-and-last [line]
  (+ (* (first-digit line) 10)
     (last-digit line)))

(defn day01-part1 [input]
  (->> input
       (map value-of-first-and-last)
       (reduce + 0)))

(deftest part-1
  (testing "Day 01 part 1 with test input"
    (is (= 142 (day01-part1 day01-test-input) )))
  (testing "Day 01 part 1 with real input"
    (is (= 54951 (day01-part1 (read-real-input "day01")) ))))

(deftest part-2
  (testing "Day 01 part 2 with test input"
    (is (= 281 (day01-part2 day01-test-input-2)))) 
  (testing "Day 01 part 2 with real input"
    (is (= 55218 (day01-part2 (read-real-input "day01"))))))

(comment
  (first-digit "1abc2")
  (last-digit "12345blah9jkdsfh")
  (read-real-input "day01")
  (System/getProperty "user.dir")


  (first-digit-or-word "two1nine")
  (map first-digit-or-word day01-test-input-2)
  (->> (read-real-input "day01")
       (map #(+
              (* 10 (first-digit-or-word %))
              (last-digit-or-word %))))

  (first-digit-or-word "tfphzkcxh3twofour9oneightg")
  (last-digit-or-word "tfphzkcxh3twofour9oneightg")


  (re-seq #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))" 
          "tfphzkcxh3twofour9oneightg")
  )
