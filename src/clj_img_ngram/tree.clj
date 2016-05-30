(ns clj-img-ngram.tree
  (:require [clojure.java.io :as io])
  (:gen-class))


(defn get-child [node id]
  (first (filter #(= (:id (second %)) id) (map-indexed vector (:children node)))))

(defn get-children [node]
  (:children node))

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


(defn generate [data tree]
  (if (empty? data)
    tree
    (recur (rest data) (add-ngram tree (first data)))))
