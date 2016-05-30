(ns clj-img-ngram.core
  (:use [mikera.image.core])
  (:require [clojure.java.io :as io]
            [mikera.image.filters :as filt]
            [clj-img-ngram.tree :as tree]
            [clj-img-ngram.ngram :as ngram]))



;; load an image from a resource file
(def ant (load-image (.getPath (-> "jan-steen.jpg" io/resource))))

;; show the iamge, after applying an "invert" filter
;(show (filter-image ant (filt/invert)))

;(show ant)

(def pixels (get-pixels ant))

;(count (:down (ngram/collect-ngrams ant 4)))
;(def tree (tree/generate (:down (ngram/collect-ngrams ant 4)) (tree/make-node "root")))


;(get-child (get-ngram-tree (take 8 (:right (collect-ngrams ant 4))) (make-node "root")) -11320525)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
