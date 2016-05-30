(ns clj-img-ngram.ngram
  (:use [mikera.image.core])
  (:require [clojure.java.io :as io]
            [mikera.image.filters :as filt])
  (:gen-class))

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

(defn collect-ngrams [img n]
  (let [pixels (get-pixels img)
        h (height img)
        w (width img)
        size (count pixels)]
      {:down (filter #(not (empty? %)) (map #(collect-down pixels % n w h '()) (range size)))
       :right (filter #(not (empty? %)) (map #(collect-right pixels % n w h '()) (range size)))}))
