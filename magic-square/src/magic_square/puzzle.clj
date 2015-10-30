(ns magic-square.puzzle)

(def values [1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0])

;; Arrange values into magic-square so all the rows, columns and diagonal add to the same 'magic' number

;; Example solution using other numbers
;; Try (magic? example) to verify the magic? function
(def example 
  [[8 1 6]
   [3 5 7]
   [4 9 2]])

;; The following three sum functions are identitical as the ones in test
(defn- sum-rows [m]
  (map #(reduce + %) m))

(defn- sum-cols [m]
  [(reduce + (map first m))
   (reduce + (map second m))
   (reduce + (map last m))])

(defn- sum-diagonals [m]
  [(+ (get-in m [0 0]) (get-in m [1 1]) (get-in m [2 2]))
   (+ (get-in m [2 0]) (get-in m [1 1]) (get-in m [0 2]))])

(defn magic? 
  "For a given square of values test if all rows, cols and diagonals sum to the same 'magic' number"
  [square]
  (and 
   (= (set (sum-rows square))
      (set (sum-cols square))
      (set (sum-diagonals square)))
   (= 1
      (count (set (sum-rows square)))
      (count (set (sum-cols square)))
      (count (set (sum-diagonals square))))))

(defn permutations
  "Return the permutations for a sequence xs"
  [xs]
  (if (seq (rest xs))
    (mapcat (fn [x] (map (fn [y] (cons x y)) (permutations (remove #{x} xs)))) xs)
    [xs]))

(defn build-square
  "Divide values into a square using 'partition 3"
  [values]
  (mapv vec (partition 3 values)))

(defn magic-square 
  "Return the first magic square built from values
  
  - Build all the permutations of values 
  - Convert them to squares
  - Filter this list over the magic-square? function
  - Return the first result
  "
  [values]
  (vec (first (filter magic? (map build-square (permutations values))))))

