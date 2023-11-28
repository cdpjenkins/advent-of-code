(ns advent.day7-test
  (:require [advent.day7 :as sut]
            [clojure.test :refer :all]))

(deftest weighted-graph-shizzle
  (testing "Bag that does contain other bags"
    (is (=
         {"light red"
          {"bright white" 1,
           "muted yellow" 2}}
         (sut/weighted-graph-node "light red bags contain 1 bright white bag, 2 muted yellow bags."))))
  (testing "Bag that contains no other bags"
    (is (=
         {"dotted black" {}}
         (sut/weighted-graph-node "dotted black bags contain no other bags."))))

  )


