(ns fox-goose-bag-of-corn.puzzle)

;; ----------------------------------------------------------------------------------------------------
;; Rules
;; fox <--> goose           can't be together without you
;; goose <--> corn          can't be together without you
;; boat == you == one       only you on the boat alone or with one other
;;
;; Note: Below animal is used as a name for each animal as well as the corn
;; ----------------------------------------------------------------------------------------------------

(def start-pos 
  [[[:fox :goose :corn :you] [:boat] []]])

(def end-pos 
  "The initial position will be converted to set components so our goal will be easier to compare in the same format
  [[[] [:boat] [:you :fox :goose :corn]]] => [#{} #{:boat} #{:fox :goose :corn :you}]
  "
  [#{} #{:boat} #{:fox :goose :corn :you}])

(defn moves-from-left
  "Return all possible moves from the left side.
  You can always move by yourself. 
  Also, you can move with each of the others (called animal here for simplicity)"
  ([pos] (apply moves-from-left pos))
  ([left boat right]
   (into    
    [[(disj left :you) (conj boat :you) right]]
    (map (fn [animal] [(disj left :you animal) (conj boat :you animal) right]) (disj left :you)))))

(defn moves-from-right 
  "Return all possible moves from the right side.
  You can always move by yourself. 
  Also, you can move with each of the others (called animal here for simplicity)"
  ([pos] (apply moves-from-right pos))
  ([left boat right]
   (into    
    [[left (conj boat :you) (disj right :you)]]
    (map (fn [animal] [left (conj boat :you animal) (disj right :you animal)]) (disj right :you)))))

(defn moves-from-boat
  "Return all possible moves from the boat.
  If you have an animal in the boat you move with it else you move by yourself.
  You can move either left or right."
  ([pos] (apply moves-from-boat pos))
  ([left boat right]
   (let [animal (first (disj boat :boat :you))] 
     (if animal
       [[(conj left :you animal) #{:boat} right]
        [left #{:boat} (conj right :you animal)]
        ]
       [[(conj left :you) (disj boat :you) right]
        [left (disj boat :you) (conj right :you)]
        ]
       ))))  

(defn next-moves' 
  "Return all the possible next moves from position.
  Where you are determines the possible next moves."
  [pos]
  (let [[left boat right] pos]
    (cond (left :you) (moves-from-left left boat right)
          (right :you) (moves-from-right left boat right)
          :else (moves-from-boat left boat right))))

(defn eatable?
  "Check the position for an eatable condition (goose eats corn, fox eats goose)
  1. You can't have a goose with a fox or a goose with corn unless you are with them
  2. You don't have to worry about the boat because you are the only one with them then"
  [pos]
  (let [[left boat right] pos]
    (cond 
      (not (left :you)) (or (and (every? left [:goose :fox]))
                            (and (every? left [:goose :corn])))
      (not (right :you)) (or (and (every? right [:goose :fox]))
                             (and (every? right [:goose :corn])))
      :else false)))

(defn visited? 
  "Return true if pos is in the path"
  [path pos]
  (some #(= pos %) path))

(defn next-moves
  "Return the valid moves for the last step in the path passed as a parameter.
  1. Get all possible moves with next-moves'
  2. Remove the eatable? moves
  3. Remove any moves which have already been visited in the path"
  [path]
  (remove #(visited? path %)
          (remove eatable?
                  (next-moves' (last path)))))

(defn search 
  "Searching the solutions space by looking for the last of path to be the goal.
  Use next-moves to get return steps to try on the path"
  [path]
  (let [pos (last path)]
    (cond
      (= end-pos pos) [path]
      :else  (let [next-moves (next-moves path)]
               (if (seq? next-moves)
                 (mapcat #(search (conj path %)) next-moves))))))

(defn convert-to-sets 
  "Convert the embedded vectors to sets.
  [[[:fox :goose :corn :you] [:boat] []]] => [(#{:you :fox :goose :corn} #{:boat} #{})]"
  [positions]
  (mapv (partial mapv set) positions))

(defn convert-to-vects
  "Convert the embedded sets back to vectors. Inverse of convert-to-sets"
  [positions]
  (mapv (partial mapv vec) positions))

(defn river-crossing-plan 
  "Search for the path through the possible moves to reach the goal.
  There are two correct paths. Return the first one as it is the one to pass the 'test'"
  []
  (let [path (convert-to-sets start-pos)
        result (search path)]
    (convert-to-vects (first result))))



;; ----------------------------------------------------------------------------------------------------
;; ----------------------------------------------------------------------------------------------------
;; Solution:
;; fox-goose-bag-of-corn.puzzle> (pprint (river-crossing-plan))
;; [[[:you :fox :goose :corn] [:boat] []]
;;  [[:fox :corn] [:you :boat :goose] []]
;;  [[:fox :corn] [:boat] [:you :goose]]
;;  [[:fox :corn] [:you :boat] [:goose]]
;;  [[:you :fox :corn] [:boat] [:goose]]
;;  [[:corn] [:you :fox :boat] [:goose]]
;;  [[:corn] [:boat] [:you :fox :goose]]
;;  [[:corn] [:you :boat :goose] [:fox]]
;;  [[:you :goose :corn] [:boat] [:fox]]
;;  [[:goose] [:you :boat :corn] [:fox]]
;;  [[:goose] [:boat] [:you :fox :corn]]
;;  [[:goose] [:you :boat] [:fox :corn]]
;;  [[:you :goose] [:boat] [:fox :corn]]
;;  [[] [:you :boat :goose] [:fox :corn]]
;;  [[] [:boat] [:you :fox :goose :corn]]]
;; ----------------------------------------------------------------------------------------------------

