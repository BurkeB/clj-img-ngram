(ns clj-img-ngram.ngram
  (:use [mikera.image.core])
  (:require [clojure.java.io :as io]
            [mikera.image.filters :as filt]
            [mikera.image.colours :as c]
            [clj-img-ngram.tree :as tree])
  (:gen-class))

(defn collect-down [pixels i n img-width img-height set]
  (if (or (= 0 n) (>= (Math/floor (/ i img-width)) img-height))
    set
    (recur pixels (+ i img-width) (dec n) img-width img-height (conj set (c/components-rgb (get pixels i))))))

(defn collect-right [pixels i n img-width img-height set]
  (if (= 0 n)
    set
    (if (>= (inc (mod i img-width)) img-width)
      (conj set (get pixels i))
      (recur pixels (inc i) (dec n) img-width img-height (conj set (c/components-rgb (get pixels i)))))))

(defn collect-ngrams [img n]
  (let [pixels (get-pixels img)
        h (height img)
        w (width img)
        size (count pixels)]
      {:down (filter #(not (empty? %)) (map #(collect-down pixels % n w h '()) (range size)))
       :right (filter #(not (empty? %)) (map #(collect-right pixels % n w h '()) (range size)))}))


(defn count-ngram [tree ngram]
  (if (empty? ngram)
    (:count tree)
    (let [child (tree/get-child tree (first ngram))]
      (if (empty? child)
      0
      (recur (second child) (rest ngram))))))

(defn probability [tree ngram]
  (let [p1 (count-ngram tree ngram)
        p2 (count-ngram tree (drop-last 1 ngram))]
    (println p1)
    (println p2)
    (if (or (= p1 0) (= p2 0))
      0
      (/ p1 p2))))

(drop-last 1 '(1 2 3 4 5))

(defn next-possibilities
  ([tree]
    (map :id (tree/get-children tree)))
  ([tree ngram]
    (if (empty? ngram)
      (map :id (tree/get-children tree))
      (let [child (tree/get-child tree (first ngram))]
        (if (empty? child)
          (next-possibilities tree '())
          (recur (second child) (rest ngram)))))))
