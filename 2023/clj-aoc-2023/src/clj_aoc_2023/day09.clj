(ns clj-aoc-2023.day09
  (:require [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]))

(defn parse-line [line]
  (->> line
       (#(s/split % #" "))
       (map parse-long)))

(defn parse-input [input]
  (map parse-line input))

(defn all-items-the-same? [xs]
  (apply = xs))

(defn differences [xs]
  (map (fn [[x1 x2]] (- x2 x1))
       (partition 2 1 xs)))

(defn extrapolate-forwards [xs]
  (if (all-items-the-same? xs)
    (first xs)
    (let [diffs (differences xs)]
      (+ (last xs) (extrapolate-forwards diffs)))))

(defn part1 [input]
  (->> input
       parse-input
       (map extrapolate-forwards)
       (reduce +)))

(defn part2 [input]
  (->> input
       parse-input
       (map reverse)
       (map extrapolate-forwards)
       (reduce +)))
