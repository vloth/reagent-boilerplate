(ns cards.core {:clj-kondo/config {:linters {:unsorted-required-namespaces {:level :off}}}}
    (:require [cljsjs.react]
              [cljsjs.react.dom]
              [malli.dev.cljs :as md]
              [cards.test.matcher-setup]
              [devcards.core :refer [start-devcard-ui!]]
              ; Cards
              [cards.wallet-history-cards]
              [cards.adapter-cards]
              [cards.simple-components]
              [cards.user-story.wallet-journey-cards]))

(defn ^:export main []
  (md/start!)
  (start-devcard-ui!))
