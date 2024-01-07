(ns clj-aoc-2023.day01)

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
