(ns clj-aoc-2023.util-test
  (:require [clj-aoc-2023.util :as sut]
            [clojure.test :refer :all]))

(deftest split-by-p-shizzle
  (testing "split-by-p" 
    (is (= [[] [1 2 3] [] [4 5 6] [] [] [7] [8]]
           (sut/split-by-p zero? [0 1 2 3 0 0 4 5 6 0 0 0 7 0 8 0])))
    
    (is (= [["hello world"
             "this is lovely"]
            ["this is another"
             "paragraph"]]
           (sut/split-by-p empty?
                            ["hello world"
                             "this is lovely"
                             ""
                             "this is another"
                             "paragraph"])))))
