(ns tiny-maze.solver)

;; ----------------------------------------------------------------------------------------------------
;; S : start of the maze
;; E : end of the maze
;; 1 : This is a wall that you cannot pass through
;; 0 : A free space that you can move through.
;;
;; The goal is the get to the end of the maze. A solved maze will have
;; a :x in the start, the path, and the end of the maze, like this.
;;
;; (def start-maze 
;;   [[:S 0 1]
;;    [1  0 1]
;;    [1  0 :E]])
;;
;; (def end-maze 
;;   [[:x :x 1]
;;    [1  :x 1]
;;    [1  :x :x]])
;; ----------------------------------------------------------------------------------------------------

(defn get-next-positions 
  "Get the valid possible next positions from pos."
  [maze pos]
  (let [size (count maze)
        directions [ [-1 0]    ;; up => [-1 0]
                     [1 0]     ;; down => [1 0]
                     [0 1]     ;; right => [0 1]
                     [0 -1] ]  ;; left => [0 -1]
        inbounds (fn [size [col row]]
                   (and (and (> col -1)
                             (> row -1))
                        (and (< col size)
                             (< row size))))]
    (filter #(inbounds size %) (map #(mapv + pos %) directions))))

(defn get-val 
  "Return the value in the maze at the position [row col]"
  [maze pos]
  (let [[r c] pos]
    (get-in maze [r c])))
              
(defn end-node? 
  "Is the value at pos :E"
  [maze pos]
  (= :E (get-val maze pos)))

(defn visited? 
  "Return true if pos is in the path"
  [path pos]
  (some #(= pos %) path))

(defn valid?
  "Test if nove is valid. Valid notes can be :S, :E or 0. A 1 is invalid."
  [maze pos]
  (let [val (get-val maze pos)]
    (or (= val :S)
        (= val :E)
        (= val 0))))

(defn next-steps 
  "For a given path return from next possible positions.

  - Get the next positions
  - Filter out invalid positions
  - Filter out previously visited positions (those in path)"
  [maze path]
  (if-let [node (last path)] 
      (remove #(visited? path %) (filter #(valid? maze %) (get-next-positions maze node)))))

(defn mark-path 
  "For each item in path use to mark an :x in maze"
  [maze path]
  (if (empty? path)
    maze
    (recur (assoc-in maze (first path) :x) (rest path))))

(defn get-path
  "Work through the maze using next-steps to find the path to the end node."
  [maze path]
  (let [node (last path)]
    (if (end-node? maze node)
      (conj path node)
      (vec (mapcat #(get-path maze (conj path %)) (next-steps maze path))))))

(defn solve-maze 
  "For a given maze find the path through the maze and then mark the path with :x"
  [maze]
  (let [path (get-path maze [[0 0]])]
    (mark-path maze path)))
