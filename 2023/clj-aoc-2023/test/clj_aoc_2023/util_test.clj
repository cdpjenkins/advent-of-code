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

(deftest test-lines-with-index-trimmed
  (testing "Can cope with blank (but not completely empty) lines"
    
    (is (some?
         (sut/lines-with-indent-trimmed "
                Look out, a blank (not not empty) line follows this
                
                Oh dear!")))))

(deftest test-gcd
  (testing "Can compute gcd of two numbers"
    (is (= 4 (sut/gcd 16 12)))
    (is (= 3 (sut/gcd 99 15)))))

(deftest test-lcm
  (testing "Can compute lcm"
    (is (= 24 (sut/lcm 12 8)))))
