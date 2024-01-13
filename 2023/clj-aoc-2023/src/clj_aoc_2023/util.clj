(ns clj-aoc-2023.util
  (:require 
            [clojure.string :as s]))

(defn read-real-input [name]
  (s/split-lines
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))

(defn split-by-p [p col]
  (let [not-p (complement p)]
    (loop [xs col
           result []]
      (if (empty? xs)
        result
        (recur (drop 1 (drop-while not-p xs))
               (conj result (take-while not-p xs)))))))

(defn- indent-level [line]
  (or
   (first
    (keep-indexed #(when (not= %2 \space) %1) line))
   nil))

  (use 'clojure.pprint)

(defn- remove-indent [line n]
  (if (<= n (count line))
    (subs line n)
    ""))

(defn lines-with-indent-trimmed [big-string]
  (let [lines (s/split-lines big-string)
        maybe-removed-start (if (empty? (first lines))
                              (drop 1 lines)
                              lines)
        no-empty-lines (filter (complement empty?) maybe-removed-start)
        min-indent (or (apply min (map indent-level no-empty-lines))
                       0)
        trimmed-lines (map #(remove-indent % min-indent) maybe-removed-start)] 
    trimmed-lines))
