(ns clj-aoc-2023.core-test
  (:require [clojure.test :refer :all]
            [clj-aoc-2023.core :refer :all]))

(def day01-test-input ["1abc2"
                       "pqr3stu8vwx"
                       "a1b2c3d4e5f"
                       "treb7uchet"])

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

(deftest a-test
  (testing "Day 01 part 1"
    (is (= (day01-part1 day01-test-input) 142))))


(comment
  (first-digit "1abc2")
  (last-digit "12345blah9jkdsfh")

  )
