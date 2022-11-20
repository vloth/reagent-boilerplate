(ns cards-node
  "Setup happy-dom globally. 
  This file should not be required for the browser bundle.
  happy-dom requires nodejs libraries and is not isomorphic."
  #_{:clj-kondo/ignore [:unsorted-required-namespaces]}
  (:require [cljsjs.react]
            [cljsjs.react.dom]
            ["@happy-dom/global-registrator" :as happy-dom]
            [malli.dev.cljs :as md]
            ; Cards
            [deck.adapter-card]
            [deck.component-card]
            [deck.wallet-history-card]
            [flow.wallet-flow-card]))

(.register happy-dom/GlobalRegistrator)
(set! (.-node? js/window) true)
(md/start!)
