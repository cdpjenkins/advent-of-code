(ns clj-aoc-2023.day02
  (:require [clojure.string :as s])
  )

(defn- parse-game-header [input]
  (let [[_ id-str] (re-matches #"^Game (\d+)$" input)]
    (Integer. id-str)))

(defn- parse-round [round-str]
  (let [[_ reds] (re-find #"(\d+) red" round-str)
        [_ greens] (re-find #"(\d+) green" round-str)
        [_ blues] (re-find #"(\d+) blue" round-str)]
    {:red (-> reds (some-> (parse-long)) (or 0)) 
     :green (-> greens (some-> (parse-long)) (or 0))
     :blue (-> blues (some-> (parse-long)) (or 0))}))

(defn- parse-rounds [input]
  (->> (s/split input #";")
       (map parse-round)))

(defn parse-game [line]
  (let [[header-str rounds-str] (s/split line #":") ] 
    {:id (parse-game-header header-str)
     :rounds (parse-rounds rounds-str)}))

(defn- round-is-valid [round]
  (and
    (<= (:red round) 12)
    (<= (:green round) 13)
    (<= (:blue round) 14)))

(defn- is-possible [game]
  (every? round-is-valid
          (:rounds game)))

(defn gimme-the-power [game]
  (let [rounds (:rounds game)
        max-red (apply max (map :red rounds))
        max-green (apply max (map :green rounds))
        max-blue (apply max (map :blue rounds))]
    (* max-red max-green max-blue))
  )

(defn day02-part1 [input]
  (->> input
       (map parse-game)
       (filter is-possible)
       (map #(:id %))
       (reduce + 0)))

(defn day02-part2 [input]
  (->> input
       (map parse-game)
       (map gimme-the-power)
       (reduce + 0)))

(comment
  (parse-round " 8 green, 6 blue, 20 red")
  (parse-rounds " 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red")
  (parse-game "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red")
  
  (prn (re-seq #"(\d+) red" " 8 green, 6 blue, 20 red")) 
  )
