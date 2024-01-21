(ns clj-aoc-2023.day08
  (:require [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]))

(defn instructions-of [input]
  (first input))

(defn node-of [line]
  (let [[_ n l r] (re-matches #"([A-Z0-9]{3}) = \(([A-Z0-9]{3}), ([A-Z0-9]{3})\)" line)] 
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
      :else (throw (RuntimeException. (str "Invalid instruction:" instruction)))))) 

(defn- path-from [network instructions start-node]
  (reductions #(move network %2 %1)
                         start-node
                         instructions))

(defn- indexes-that-hit-end-nodes [end-node? path]
  (keep-indexed #(when (end-node? %2) %1) path))

(defn cycle-time [network instructions node]
  (let [path (path-from network instructions node)
        [t1 t2] (take 2 (indexes-that-hit-end-nodes #(s/ends-with? % "Z") path))]
    (if (= (* t1 2) t2)
      t1
      ;; This algorithm relies on the fact that we'll reach an end node after
      ;; t1 steps and then will reach it again after t1 more steps. It's
      ;; rather fortunate that the input has this property - it would not do
      ;; in the general case, which would make this problem much harder to
      ;; solve.
      (throw (RuntimeException. (str "urghghgh" t1 t2))))))

(defn part1 [input]
  (let [instructions (cycle (instructions-of input))
        network (network-of input)
        path (path-from network instructions "AAA")]
    (first (indexes-that-hit-end-nodes #(= % "ZZZ") path))))

(defn part2 [input]
    (let [instructions (cycle (instructions-of input))
          network (network-of input)
          start-nodes (filter #(s/ends-with? % "A") (keys network))
          cycle-times (map #(cycle-time network instructions %) start-nodes)] 
      (reduce lcm cycle-times)))
