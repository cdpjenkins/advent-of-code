(ns clj-aoc-2023.day08
  (:require [clj-aoc-2023.util :refer :all]
            ))

(defn instructions-of [input]
  (first input))

(defn node-of [line]
  (let [[_ n l r] (re-matches #"([A-Z]{3}) = \(([A-Z]{3}), ([A-Z]{3})\)" line)] 
       [n [l r]]))

(defn network-of [input]
  (->> input
       (drop 2)
       (map node-of)
       (into {})))

(defn move [network instruction node]
  (let [[left right] (network node)]
    (condp = instruction
      \L left
      \R right
      :else (throw (RuntimeException. (str "Invalid instruction:]" instruction)))))) 

(defn part1 [input] 
  (let [instructions (cycle (instructions-of input))
        network (network-of input)
        path (reductions #(move network %2 %1)
                         "AAA"
                         instructions)] 
    (first (keep-indexed #(when (= %2 "ZZZ") %1) path))))

(comment
  (take 10 (part1 test-input))

  (def test-input
    (lines-with-indent-trimmed "
        RL
        
        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)"))

  (part1 test-input)
  (println "poo")
  

  (instructions-of test-input)
  (def network
    (network-of test-input))

  (require '[clojure.pprint :as pp])
  (pp/pprint (network "AAA"))

  )

(defn part2 [input]
  
  
  )
