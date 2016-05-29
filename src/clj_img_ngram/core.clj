(ns clj-img-ngram.core
  (:use [mikera.image.core])
  (:require [clojure.java.io :as io]
            [mikera.image.filters :as filt])
  (:gen-class))



;; load an image from a resource file
(def ant (load-image (.getPath (-> "jan-steen.jpg" io/resource))))

;; show the iamge, after applying an "invert" filter
(show (filter-image ant (filt/invert)))

(show ant)

(def pixels (get-pixels ant))


(defn collect-ngrams [img n]
  (let [pixels (get-pixels img)
        h (height img)
        w (width img)
        size (count pixels)]
      {:down (filter #(not (empty? %)) (map #(collect-down pixels % n w h '()) (range size)))
       :right (filter #(not (empty? %)) (map #(collect-right pixels % n w h '()) (range size)))}))

(time (collect-ngrams ant 4))

(defn collect-down [pixels i n img-width img-height set]
  (if (or (= 0 n) (>= (Math/floor (/ i img-width)) img-height))
    set
    (recur pixels (+ i img-width) (dec n) img-width img-height (conj set (get pixels i)))))

(defn collect-right [pixels i n img-width img-height set]
  (if (= 0 n)
    set
    (if (>= (inc (mod i img-width)) img-width)
      (conj set (get pixels i))
      (recur pixels (inc i) (dec n) img-width img-height (conj set (get pixels i))))))


(defn get-child [node id]
  (first (filter #(= (:id (second %)) id) (map-indexed vector (:children node)))))

(defn make-node [id]
  {:id id :count 1 :children []})

(defn add-ngram [tree ngram]
  (let [tree (assoc tree :count (inc (:count tree)))]
  (if (empty? ngram)
    tree
    (let [child (get-child tree (first ngram))]
      (if (empty? child)
        (assoc tree :children (conj (:children tree) (add-ngram (make-node (first ngram)) (rest ngram))))
        (assoc tree :children (assoc (:children tree) (first child) (add-ngram (second child) (rest ngram))))
        )))))


(defn get-ngram-tree [data tree]
  (if (empty? data)
    tree
    (recur (rest data) (add-ngram tree (first data)))))

(get-ngram-tree (take 90 (:down (collect-ngrams ant 4))) (make-node "root"))

(get-child (get-ngram-tree (take 8 (:right (collect-ngrams ant 4))) (make-node "root")) -11320525)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
