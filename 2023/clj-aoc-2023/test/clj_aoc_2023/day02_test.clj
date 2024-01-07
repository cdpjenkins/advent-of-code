(ns clj-aoc-2023.day02-test
  (:require [clj-aoc-2023.day02 :as sut]
            [clojure.string :as s]
            [clojure.test :refer :all]))

(def day02-test-input
  ["Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
   "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"
   "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"
   "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"
   "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"])

(defn read-real-input [name]
  (s/split-lines
   (slurp (str "../../advent-of-code-input/2023/" name ".txt"))))

(deftest part-1
  (testing "Day 01 part 1 with test input"
    (is (= 8 (sut/day02-part1 day02-test-input))))
    (testing "Day 01 part 1 with real input"
    (is (= 2439 (sut/day02-part1 (read-real-input "day02"))))))

(deftest part-2
  (comment
    (testing "Day 01 part 2 with test input"
      (is (= -1 (sut/day02-part2 day02-test-input))))

    (testing "Day 01 part 2 with real input"
      (is (= -1 (sut/day02-part2 (read-real-input "day02")))))))

(comment
  (sut/day02-part1 day02-test-input)

  (parse-long "argh")
  )
