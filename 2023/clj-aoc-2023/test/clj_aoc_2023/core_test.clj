(ns clj-aoc-2023.core-test
  (:require [clojure.test :refer :all]
            [clj-aoc-2023.core :refer :all]))

(def day01-test-input ["1abc2"
                       "pqr3stu8vwx"
                       "a1b2c3d4e5f"
                       "treb7uchet"])

(defn first-digit [line]
  (Integer. (first (re-seq #"[0-9]" line))))

(defn last-digit [line]
  (Integer. (last (re-seq #"[0-9]" line))))

(defn value-of-firstT-and-last [line]
  (+ (* (first-digit line) 10)
     (last-digit line)))

(defn day01-part1 [input]
  (reduce + 0 
          (map  value-of-first-and-last input)))


(deftest a-test
  (testing "Day 01 part 1"
    (is (= (day01-part1 day01-test-input) 142))))


(comment
  (first-digit "1abc2")
  (last-digit "12345blah9jkdsfh")
  )
