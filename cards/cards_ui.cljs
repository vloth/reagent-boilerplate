(ns cards-ui
    #_{:clj-kondo/ignore [:unsorted-required-namespaces]}
    (:require [cljsjs.react]
              [cljsjs.react.dom]
              [malli.dev.cljs :as md]
              [aux.matcher-setup]
              [devcards.core :refer [start-devcard-ui!]]
              ; Cards
              [deck.adapter-card]
              [deck.component-card]
              [deck.wallet-history-card]
              [flow.wallet-flow-card]))

(defn ^:export main []
  (md/start!)
  (start-devcard-ui!))
