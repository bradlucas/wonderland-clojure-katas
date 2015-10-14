(ns doublets.solver
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.set :as set]))

(def words (-> "words.edn"
               (io/resource)
               (slurp)
               (read-string)))

;; ----------------------------------------------------------------------------------------------------
;; Approach
;; filter dictionary to same length words 
;; function to return words with only one letter that is different for a given word
;; function to check if word is in current path/collection
;; ----------------------------------------------------------------------------------------------------

(defn find-same-length-words 
  "Return all words with the same length as word"
  [word dict]
  (let [len (count word)]
  (filter #(= (count %) len) dict)))

;; (defn one-letter-diff0?
;;   "Return true of word1 and word2 differ by one character. Assumes same length words
;;   Note: the change has to happen in the same position so this function doesn't work
;;   "
;;   [word1 word2]
;;   (let [s1 (set word1)
;;         s2 (set word2)]
;;     (and (= 1 (count (set/difference s1 s2)))
;;          (= 1 (count (set/difference s2 s1))))))

(defn one-letter-diff?
  "Return true of word1 and word2 differ by one character. Assumes same length words
  The change has to happen in the same position so we map over both words checking for changes"
  [word1 word2]
    (= 1 (count (filter true? (map #(not= %1 %2) word1 word2)))))

(defn find-one-letter-diffs
  "Return all the words with one letter different in words"
  [word words]
  (filter #(one-letter-diff? word %) words))

(defn contains-value? 
  "Does the collection contain the word"
  [word coll]
  (some #(= word %) coll))

(defn find-paths
  [word1 word2 path dict]
  (let [next-words (find-one-letter-diffs word1 dict)]
    (if (or (empty? next-words) (contains-value? word1 path))
      (vector path)
      (mapcat #(find-paths %1 word2 (conj path word1) dict) next-words))))

(defn doublets [word1 word2]
  (let [dict (find-same-length-words word1 words)]
    (let [start word1 end word2]
      (let [result (filter #(and (= (first %) word1) (= (last %) word2)) (find-paths start end [] dict))]
        (if (seq result)
          (first result)
          [])))))

