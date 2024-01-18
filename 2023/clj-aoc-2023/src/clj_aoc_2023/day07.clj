(ns clj-aoc-2023.day07
  (:require [clj-aoc-2023.util :refer :all]))

(defn- parse-row [line]
  (let [[_ hand-str bid-str] (re-matches #"([23456789TJQKA]{5}) (\d+)" line)] 
    [hand-str (parse-long bid-str)]))

(defn hand-type [^String hand]
  (let [freqs (->> hand 
                   (frequencies)
                   (map val)
                   (sort)
                   (reverse))]
    (condp = freqs
      [5]   :five-of-a-kind
      [4 1] :four-of-a-kind
      [3 2] :full-house
      [3 1 1] :three-of-a-kind
      [2 2 1] :two-pair
      [2 1 1 1] :one-pair
      [1 1 1 1 1] :high-card)))

(def hand-type-value {:five-of-a-kind   7
                      :four-of-a-kind   6
                      :full-house       5
                      :three-of-a-kind  4
                      :two-pair         3
                      :one-pair         2
                      :high-card        1})

(def card-value-part1 {\2 2
                 \3 3
                 \4 4
                 \5 5
                 \6 6
                 \7 7
                 \8 8
                 \9 9
                 \T 10
                 \J 11
                 \Q 12
                 \K 13
                 \A 14})

(def card-value-part2 (assoc card-value-part1
                             \J 1))

(defn hand-value [hand card-value-fn]
  (let [v (->> hand
               (hand-type)
               (hand-type-value))
        card-values (map card-value-fn hand)]
    (apply vector (cons v card-values))))

(defn hand-value2 [hand bestest-hand]
  (let [v (->> bestest-hand
               (hand-type)
               (hand-type-value))
        card-values (map card-value-part2 hand)]
    (apply vector (cons v card-values))))

(defn hand-value-part1 [hand]
  (hand-value hand card-value-part1))

(defn hand-value-part2 [hand]
  (if (= hand "JJJJJ")
    (hand-value "AAAAA" card-value-part2)
    (let [num-jokers (count (filter #(= % \J) hand))
          without-jokers (filter #(not= % \J) hand)
          highest-freq-card (->> without-jokers
                                 frequencies
                                 (apply max-key val)
                                 key)
          most-valuable-hand (concat without-jokers (repeat num-jokers highest-freq-card))] 
      (hand-value2 hand most-valuable-hand))))

(defn part1 [input]
  (let [hands-n-bids  (map parse-row input)
        sorted-respect-due (sort-by #(hand-value-part1 (first %)) hands-n-bids)
        ston (map vector (drop 1 (range)) sorted-respect-due)]
    (reduce + (map (fn [[i [h b]]] (* i b)) ston))))

(defn part2 [input]
  (let [hands-n-bids  (map parse-row input)
        sorted-respect-due (sort-by #(hand-value-part2 (first %)) hands-n-bids)
        ston (map vector (drop 1 (range)) sorted-respect-due)]
    (reduce + (map (fn [[i [h b]]] (* i b)) ston))))

