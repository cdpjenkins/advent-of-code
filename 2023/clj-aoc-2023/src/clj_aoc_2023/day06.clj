(ns clj-aoc-2023.day06
  (:require [clj-aoc-2023.util :refer :all]
            [clojure.string :as s]
            [clojure.math :as math]))

(defn parse-part1-style [line]
  (->> (s/split line #":") 
       (second)
       (s/trim)
       (#(s/split % #" +"))
       (map parse-long)))

(defn parse-part2-style [line]
  (->> (s/split line #":")
       (second)
       (s/trim)
       (#(s/replace % " " ""))
       (#(s/split % #" +"))
       (map parse-long)))

(defn solve-quadratic [time distance]
  (let [root1 (/ (- time (math/sqrt (- (* time time) (* 4 distance)))) 2)
        root2 (/ (+ time (math/sqrt (- (* time time) (* 4 distance)))) 2)]
    [root1 root2]))

(defn- is-integer? [x]
  (= (math/ceil x) (math/floor x)))

(defn num-ways-you-could-beat-record [[time distance]]
  (let [[root1 root2] (solve-quadratic (double time) (double distance))
        begin (int (if (is-integer? root1)
                     (math/ceil (+ 1 root1))
                     (math/ceil root1)))
        end (int (math/ceil root2))]
    (- end begin)))

(defn part1 [input]
  (let [games (map vector
                   (parse-part1-style (first input))
                   (parse-part1-style (second input)))]
    (apply * (map num-ways-you-could-beat-record games))))

(defn part2 [input]
  (let [games (map vector
                   (parse-part2-style (first input))
                   (parse-part2-style (second input)))]
    (apply * (map num-ways-you-could-beat-record games))))

