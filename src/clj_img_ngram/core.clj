(ns clj-img-ngram.core
  (:use [mikera.image.core])
  (:require [clojure.java.io :as io]
            [mikera.image.filters :as filt]
            [mikera.image.colours :as c]
            [clj-img-ngram.tree :as tree]
            [clj-img-ngram.ngram :as ngram]))



;; load an image from a resource file
(def ant (load-image (.getPath (-> "jan-steen.jpg" io/resource))))

;; show the iamge, after applying an "invert" filter
;(show (filter-image ant (filt/invert)))

(show ant)

(def pixels (get-pixels ant))

;(count (:down (ngram/collect-ngrams ant 4)))
;(def tree (tree/generate (take 100000 (:down (ngram/collect-ngrams ant 4))) (tree/make-node "root")))

;(ngram/probability tree (take 2 (first (:down (ngram/collect-ngrams ant 4)))))
;(ngram/probability tree (second (:down (ngram/collect-ngrams ant 4))))


;(count pixels)
;(ngram/next-possibilities tree (take 1 (first (:down (ngram/collect-ngrams ant 4)))))
;(ngram/next-possibilities tree '())

(defn change-pixels [pixels i]
  (let [seq (map c/components-rgb (list (get pixels (- i 1)) (get pixels (- i 2))))
        next (first (ngram/next-possibilities tree seq))]
    (aset pixels i (apply c/rgb next))
    (if (>= i (dec (count pixels)))
      pixels
      (recur pixels (inc i)))))
;; wrong color values in "next"...

;(change-pixels pixels 100000)
;(set-pixels ant pixels)
;(show ant)
;(ngram/next-possibilities tree)
;(get-child (get-ngram-tree (take 8 (:right (collect-ngrams ant 4))) (make-node "root")) -11320525)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
