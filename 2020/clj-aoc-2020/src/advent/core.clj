(ns advent.core
  (:require [clojure.string :as s])
  (:require [clojure.set :as cs])
  )

(comment
  (def input1 (map #(Integer/parseInt %)(s/split-lines (slurp "input1.txt"))))
  (count
   (let [input (slurp "input2.txt")
         lines (s/split-lines input)]
     (last lines)
     (filter line-is-valid lines)))
  )

(comment

  (defn line-is-valid [line]
           (let [[policy password] (s/split line #": ")
                 [times char] (s/split policy #" ")

                 [pos1 pos2] (map #(Integer/parseInt %) (s/split times #"\-"))
                 pos1-contains (= (str (get password (dec pos1)) ) char)
                 pos2-contains (= (str (get password (dec pos2)) ) char)]


             (println "stour")
             (println char)
             (println "pos1: " pos1)
             (println (get password (dec pos1)))
             (println pos1-contains)
             (println "pos2: " pos2)
             (println (get password (dec pos2)))
             (println pos2-contains)
             (and
              (not (and pos1-contains pos2-contains))
              (or pos1-contains pos2-contains)))))

(comment
  (line-is-valid "1-2 w: wmvmhmmmxddzmmmm")
  (get "01234" 2)
  


  (defn read-input3[]
    (let [input (slurp "input3.txt")
          lines (apply vector (s/split-lines input))]
      lines))

  (defn tree-at? [lines x y]
    (println x y)
    (let [line (lines y)
          square (get line (mod x (count line)))]
      (= square \#)))

  (defn- trees-encountered [dx dy]
    (count
     (filter identity
             (for [i (range (quot (count lines) dy))]
               (let [x (* i dx)
                     y (* i dy)]
                 (tree-at? lines x y)))))))

(comment
  (def lines (read-input3))
  (tree-at? lines 3 2)

  (println
   (*
    (trees-encountered 1 1)
    (trees-encountered 3 1)
    (trees-encountered 5 1)
    (trees-encountered 7 1)
    (trees-encountered 1 2)))
  )

(defn- id-cards [input4]
  (s/split input4 #"\n{2,}"))

(defn- make-card
  [card-str]
  (into {}
         (map #(s/split % #":")
              (re-seq #"[a-zA-Z]{3}:[^\s]+" card-str))))

(defn- valid-field? [card field-name regex valid-fn]
  (let [field (card field-name)]
    (and
     (not (nil? field))
     (re-matches regex field)
     (valid-fn field))))

(defn- valid-card? [card]
  (and
   (valid-field? card "byr" #"\d{4}" #(<= 1920 (Integer/parseInt %) 2002))
   (valid-field? card "iyr" #"\d{4}" #(<= 2010 (Integer/parseInt %) 2020))
   (valid-field? card "eyr" #"\d{4}" #(<= 2020 (Integer/parseInt %) 2030))
   (valid-field? card "hgt" #"(\d+)(in|cm)" (fn [value]
                                              (if (s/ends-with? value "in")
                                                (<= 59 (Integer/parseInt
                                                        (s/replace value "in" "")) 76)
                                                (<= 150 (Integer/parseInt
                                                         (s/replace value "cm" "")) 193))))
   (valid-field? card "hcl" #"#[0-9a-f]{6}" (constantly true))
   (valid-field? card "ecl" #"amb|blu|brn|gry|grn|hzl|oth" (constantly true))
   (valid-field? card "pid" #"[0-9]{9}" #(= (count %) 9))))

(comment
  (def input4 (slurp "input4.txt"))
  (def cards (id-cards input4))

  (valid-card? (first (map make-card cards)))

  (count
   (filter valid-card?
           (map make-card cards)))
  FBFBBFFRLR
  0101100101

  (def seat-ids (set (map #(Long/parseLong % 2)
                         (->
                          (slurp "input5.txt")
                          (s/replace #"[FL]" "0")
                          (s/replace #"[RB]" "1")
                          (s/split #"\n")))))
  (apply max seat-ids)



  (doseq [i (sort (cs/difference (set (range 1024))
                               seat-ids))]
    (println i))

  )

(comment

  (def input6 (slurp "input6.txt"))
  (defn- groups [input]
    (map #(count (set (s/replace % #"\s" "")))
         (s/split input #"\n{2,}")))

  (reduce + (groups input6))

  (defn- gropos [input]
    (s/split input #"\n{2,}"))

  (defn- process-gropo [gropo]
    (count
         (apply cs/intersection
                (map set
                     (s/split gropo #"\n")))))

  (reduce +
          (map process-gropo
               (gropos (slurp "input6.txt"))))


  
  )




