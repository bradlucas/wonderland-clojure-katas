(ns card-game-war.game-test
  (:require [clojure.test :refer :all]
            [card-game-war.game :refer :all]))

(deftest test-play-round
  (testing "the highest rank wins the cards in the round"
    ;; mock up a sample hand where we know the top cards then verify the winner gets the right cards
    (is (let [player1-cards [[:heart 2] [:heart 4] [:diamond 4] [:heart 7] [:club 8] [:spade :queen] [:heart :king] [:heart :jack] [:club 3] [:heart 5] [:spade 6] [:diamond 5] [:club 5] [:spade 7] [:diamond :king] [:club 2] [:club 10] [:heart 10] [:diamond 7] [:diamond 10] [:club :queen] [:heart 8] [:spade 10] [:spade 9] [:club 4] [:diamond 9]] 
              player2-cards [[:club :jack] [:heart 3] [:club :ace] [:spade :king] [:spade 5] [:spade 3] [:heart :queen] [:club 7] [:spade :jack] [:club :king] [:diamond :ace] [:club 6] [:spade 8] [:heart 9] [:diamond :queen] [:diamond 6] [:spade 2] [:diamond 8] [:heart :ace] [:heart 6] [:diamond 2] [:diamond 3] [:spade 4] [:diamond :jack] [:club 9] [:spade :ace]]
              [player1-cards player2-cards] (play-round player1-cards player2-cards)]
          (and
           (= player1-cards [[:heart 4] [:diamond 4] [:heart 7] [:club 8] [:spade :queen] [:heart :king] [:heart :jack] [:club 3] [:heart 5] [:spade 6] [:diamond 5] [:club 5] [:spade 7] [:diamond :king] [:club 2] [:club 10] [:heart 10] [:diamond 7] [:diamond 10] [:club :queen] [:heart 8] [:spade 10] [:spade 9] [:club 4] [:diamond 9]] )
           (= player2-cards [[:heart 3] [:club :ace] [:spade :king] [:spade 5] [:spade 3] [:heart :queen] [:club 7] [:spade :jack] [:club :king] [:diamond :ace] [:club 6] [:spade 8] [:heart 9] [:diamond :queen] [:diamond 6] [:spade 2] [:diamond 8] [:heart :ace] [:heart 6] [:diamond 2] [:diamond 3] [:spade 4] [:diamond :jack] [:club 9] [:spade :ace] [:club :jack] [:heart 2]])))))

  (testing "queens are higher rank than jacks"
    (is (card-value-greater [:spade :queen] [:spade :jack])))

  (testing "kings are higher rank than queens"
    (is (card-value-greater [:club :king] [:club :queen])))

  (testing "aces are higher rank than kings"
    (is (card-value-greater [:club :ace] [:diamond :king])))

  (testing "if the ranks are equal, clubs beat spades"
    (is (card-value-greater [:club 4] [:spade 4])))

  (testing "if the ranks are equal, diamonds beat clubs"
    (is (card-value-greater [:diamond 9] [:club 9])))

  (testing "if the ranks are equal, hearts beat diamonds"
    (is (card-value-greater [:heart :jack] [:diamond :jack]))))

(deftest test-play-game
  (testing "the player loses when they run out of cards")
  (is (let [[player1-cards player2-cards] (play-game)]
        (or (is-loser player1-cards) 
            (is-loser player2-cards)))))

