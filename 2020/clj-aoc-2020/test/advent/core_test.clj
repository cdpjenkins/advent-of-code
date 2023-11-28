(ns advent.core-test
  (:require [clojure.test :refer :all]
            [advent.day7 :refer :all]))

(deftest weighted-graph-shizzle
  (testing "Bag that does contain other bags"
    (is (=
         {"light red"
          {"bright white" 1,
           "muted yellow" 2}}
         (weighted-graph-node "light red bags contain 1 bright white bag, 2 muted yellow bags.")))))

