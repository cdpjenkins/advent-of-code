(ns clj-aoc-2023.day03
  (:require [clojure.string :as s])
  )

(defn parse-line [y line]
  (let [matcher (re-matcher #"\d+" line)]
    (loop [nums #{}]
      (if (.find matcher)
        (do
          (recur (conj nums {:begin-x (.start matcher)
                             :end-x (.end matcher)
                             :y y
                             :num (parse-long (.group matcher))})))
        nums)))
  )

(defn- parse-symbols [y line]
  (keep-indexed (fn [x c]
                 (if (and (not= c \.)
                          (not (#{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9} c)))
                   [x y]
                   nil))
                line))

(defn- parse-stars [y line]
  (keep-indexed (fn [x c]
                  (if (= \* c)
                    [x y]
                    nil))
                line))

(defn adjacent-to-symbol [number sym]
  (and
   (>= (second sym) (dec (:y number)))
   (<= (second sym) (inc (:y number)))
   (>= (first sym) (dec (:begin-x number)))
   (<= (first sym) (:end-x number))))

(defn part-numbers-in [input] 
  (let [numbers (apply concat
                       (map-indexed parse-line input))
        symbols (apply concat
                       (map-indexed parse-symbols input))
        part-numbers-list  (for [number numbers
                                 sym symbols
                                 :when (adjacent-to-symbol number sym)]
                             number)
        part-numbers (set part-numbers-list)]
    part-numbers))

(defn gear-ratio [star part-numbers]
  (let [adjacent-part-numbers
        (for [part-number part-numbers
              :when (adjacent-to-symbol part-number star)]
          part-number)]
    (if (= 2 (count adjacent-part-numbers))
      (* (:num (first adjacent-part-numbers))
         (:num (second adjacent-part-numbers)))
      0)))

(defn day03-part1 [input]
  (->> (part-numbers-in input)
       (map :num)
       (reduce + 0)))

(defn day03-part2 [input] 
  (let [part-numbers (part-numbers-in input)
        stars (apply concat (map-indexed parse-stars input))]

    (reduce + 0 (for [star stars]
                  (gear-ratio star part-numbers)))))

(comment
  (use 'clojure.pprint)

  (pprint
   (let [input
         ["467..114.."
          "...*......"
          "..35..633."
          "......#..."
          "617*......"
          ".....+.58."
          "..592....."
          "......755."
          "...$.*...."
          ".664.598.."]]
     (day03-part1 input)))

  (parse-symbols 5 ".*..12...*")

  (for [part-number [1 2 3]
        :when (adjacent-to-symbol part-number [1 2])])

  )
