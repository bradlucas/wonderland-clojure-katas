(ns fox-goose-bag-of-corn.puzzle)

;; ----------------------------------------------------------------------------------------------------
;; Rules
;; fox <--> goose           can't be together without you
;; goose <--> corn          can't be together without you
;; boat == you == one       only you on the boat alone or with one other
;; ----------------------------------------------------------------------------------------------------

(def start-pos [[[:fox :goose :corn :you] [:boat] []]])
;; [#{:you :fox :goose :corn} #{:boat} #{}]

(def end-pos [#{} #{:boat} #{:fox :goose :corn :you}])

;; Manually created crossing-plan
(def crossing-plan
  [ ;; winning path
   [#{:you :fox :goose :corn}         #{:boat}                        #{}]

   ;; bring goose over
   [#{:fox :corn}                     #{:boat :you :goose}            #{}]
   [#{:fox :corn}                     #{:boat}                        #{:you :goose}]
   ;; return
   [#{:fox :corn}                     #{:boat :you}                   #{:goose}]
   [#{:fox :corn :you}                #{:boat}                        #{:goose}]

   ;; bring fox over
   [#{:corn}                          #{:boat :you :fox}              #{:goose}]
   [#{:corn}                          #{:boat}                        #{:goose :you :fox}]
   ;; bring back the goose
   [#{:corn}                          #{:boat :you :goose}            #{:fox}]
   [#{:corn :you :goose}              #{:boat}                        #{:fox}]

   ;; bring over the corn
   [#{:goose}                         #{:boat :you :corn}             #{:fox}]
   [#{:goose}                         #{:boat}                        #{:fox :you :corn}]
   ;; return
   [#{:goose}                         #{:boat :you}                   #{:fox :corn}]
   [#{:goose :you}                    #{:boat}                        #{:fox :corn}]
   
   ;; bring over the corn
   [#{}                               #{:boat :you :goose}            #{:fox :corn}]
   [#{}                               #{:boat}                        #{:fox :goose :corn :you}]
])

(defn river-crossing-plan []
  crossing-plan)


