(ns cards.test.setup
  "Setup happy-dom globally. 
  This file should not be required for the browser bundle.
  happy-dom requires nodejs libraries and is not isomorphic."
  #_{:clj-kondo/ignore [:unsorted-required-namespaces]}
  (:require [cljsjs.react]
            [cljsjs.react.dom]
            ["@happy-dom/global-registrator" :as happy-dom]
            [malli.dev.cljs :as md]
            [cards.adapter-cards]
            [cards.simple-components-cards]
            [cards.user-story.wallet-journey-cards]
            [cards.wallet-history-cards]))

(.register happy-dom/GlobalRegistrator)

(set! (.-node? js/window) true)

(md/start!)
