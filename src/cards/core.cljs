(ns cards.core 
    #_{:clj-kondo/ignore [:unsorted-required-namespaces]}
    (:require [cljsjs.react]
              [cljsjs.react.dom]
              [malli.dev.cljs :as md]
              [cards.test.matcher-setup]
              [devcards.core :refer [start-devcard-ui!]]
              [cards.adapter-cards]
              [cards.simple-components-cards]
              [cards.user-story.wallet-journey-cards]
              [cards.wallet-history-cards]))

(defn ^:export main []
  (md/start!)
  (start-devcard-ui!))
