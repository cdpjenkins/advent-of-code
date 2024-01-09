(ns clj-aoc-2023.day04
  (:require [clojure.string :as s])
  )

(defn parse-card [line]
  (let [[_ card-num winning-str my-str](re-find #"^Card +(\d+):([^|]+)\|(.*)$" line)
        winning-numbers (-> winning-str (s/trim) (s/split #" +") (set))
        my-numbers (-> my-str (s/trim) (s/split #" +") (set))]
    [card-num winning-numbers my-numbers]))

;; Slightly dodgy power of 2 that maps 0 to 0...
(defn- pow2 [x]
  (cond
    (< x 0 ) 0
    (= x 0) 1
    :else (* 2 (pow2 (dec x)))))

(defn- score [[_ winning-numbers my-numbers]]
  (let [my-winning-numbers (filter winning-numbers my-numbers)]
    (pow2 (dec (count my-winning-numbers)))))

(defn day04-part1 [input]
  (->> input
       (map parse-card)
       (map score)
       (reduce +)))

(defn day04-part2 [input] 
  1234
  )

(comment
  (use 'clojure.pprint)

  (def card1 "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53")

  (day04-part1 input)

  (pprint
   (parse-card card1))

  (pprint
   (parse-card "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19"))

  

  (re-find #"^Card (1):([^|]+)\|(.*)$"  "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19")

  (pprint
   (s/split-lines input))

  (def input
"Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11")

  (println input)

  )
