(ns advent.day7
  (:require [clojure.string :as s])
  (:require [clojure.set :as cs])
  )

(comment

  (import java.util.Calendar)


  (def calendar (Calendar/getInstance))
  (.get calendar Calendar/ZONE_OFFSET)


  (import java.time.ZoneId)
  (=
   (ZoneId/systemDefault)
   (ZoneId/of "Europe/London"))

  ,)

(defn- parse-line [line]
  (let [[bag contents] (s/split line #" bags contain ")
         content-bags (->> (s/split contents #", ")
             (map #(re-find #"[1-9] ([a-z]+ [a-z]+) bags?" %))
             (map second))]
    [bag (set content-bags)]))

(def graph
  (into {}
        (map parse-line
             (s/split (slurp "input7.txt") #"\n"))))

(defn contains-bag? [containing-bag contained-bag]
  ((graph containing-bag) contained-bag))

(defn- bags-containing [bag]
  (filter #(contains-bag? % bag) (keys graph)))

(defn- bags-reachable-from [bags]
  (cs/union bags
            (set (mapcat bags-containing bags))))

(defn- parse-line-weighted [line]
  (let [[bag contents] (s/split line #" bags contain ")
        content-bags (->> (s/split contents #", ")
                          (map #(re-find #"[1-9] ([a-z]+ [a-z]+) bags?" %))
                          (map second))]
    [bag (set content-bags)])
  )

(defn weighted-graph-node [line]
  (let [[bag contents] (s/split line #" bags contain ")
        content-bags (->> (s/split contents #", ")
                          (map #(re-find #"([1-9]) ([a-z]+ [a-z]+) bags?" %))
                          (filter #(not (nil? %)))
                          (map (fn [groups]
                                 [(get groups 2) (Integer/parseInt (get groups 1))]))
                          (into {})
                          )]
    (hash-map bag content-bags))
  )

(def weighted-graph
  (apply merge (map weighted-graph-node
                    (s/split (slurp "input7.txt") #"\n"))))

(defn num-bags-contained [bag]
  (+ 1
     (reduce +
             (for [[sub-bag number] (weighted-graph bag)]
               (* number (num-bags-contained sub-bag))))))

(comment
  (println weighted-graph)



  (for [ston {"one" 1, "two" 2}]
   ston)
  )
