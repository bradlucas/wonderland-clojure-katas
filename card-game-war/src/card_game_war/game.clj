(ns card-game-war.game)

(def suits [:spade :club :diamond :heart])
(def ranks [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])
(def cards
  (for [suit suits
        rank ranks]
    [suit rank]))

;; There are two players.
;; The cards are all dealt equally to each player.
;; shuffle cards and split them between the two players
(defn deal-cards [] (partition (/ (count cards) 2) (shuffle cards)))

;; Each round, 
;;     player 1 lays a card down face up at the same time that player 2 lays a card down face up. 
;;     Whoever has the highest value card, wins both round and takes both cards.
;;
;; The winning cards are added to the bottom of the winners deck.
;;
;; Aces are high.
;; :ace == 14
;;
;; If both cards are of equal value, then the winner is decided upon by the highest suit. 
;; The suits ranks in order of ascending value are spades, clubs, diamonds, and hearts.

(defn get-rank [card]
  (second card))

(defn get-suit [card]
  (first card))

(defn card-value-greater 
  "Determine if card1 is of a greater value than card2"
  [card1 card2]
  (let [rank-rank {2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10 :jack 11 :queen 12 :king 13 :ace 14}
        rank-value1 (rank-rank (get-rank card1))
        rank-value2 (rank-rank (get-rank card2))]
    (if (= rank-value1 rank-value2)
      (let [suit-rank {:spade 1 :club 2 :diamond 3 :heart 4}
            suit-value1 (suit-rank (get-suit card1))
            suit-value2 (suit-rank (get-suit card2))]
        (> suit-value1 suit-value2))
      (> rank-value1 rank-value2))))

(defn deal-card [cards]
  [(first cards) (vec (rest cards))])

(defn play-round 
  [player1-cards player2-cards]
  (let [[card1 player1-cards] (deal-card player1-cards)
        [card2 player2-cards] (deal-card player2-cards)]
    (if (card-value-greater card1 card2)
      (let [cards1 (conj player1-cards card1 card2)
            cards2 player2-cards]
        [cards1 cards2])
      (let [cards1 player1-cards
            cards2 (conj player2-cards card2 card1)]
        [cards1 cards2]))))

(defn have-winner
  "When one player runs out of cards he loses and the other is a winner"
  [cards1 cards2]
  (or (= (count cards1) 0) (= (count cards2) 0)))

(defn is-loser
  "Check if a player's cards are gone"
  [cards]
  (= (count cards) 0))

(defn report-end-game
  "Report on the end of the game.
  Assumes we've already checked that we have a loser."
  [[player1-cards player2-cards]]
  (if (is-loser player2-cards)
     (println "Player 1 won with the following cards:\n" player1-cards)
     (println "Player 2 won with the following cards:\n" player2-cards)))

(defn play-game
  "Deal each player thei set of cards then loop over the play-round
  function until one player runs out of cards The player that runs out
  of cards loses."
  []
  (loop [[player1-cards player2-cards] (deal-cards)]
    (if (or (is-loser player1-cards) (is-loser player2-cards))
      [player1-cards player2-cards]
      (recur (play-round player1-cards player2-cards)))))

(defn play-game-report
  "Run a game and report the reports"
  []
  (report-end-game (play-game)))


