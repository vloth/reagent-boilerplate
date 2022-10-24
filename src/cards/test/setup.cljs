(ns cards.test.setup
  "Setup happy-dom globally. 
  This file should not be required for the browser bundle.
  happy-dom requires nodejs libraries and is not isomorphic."
  (:require ["@happy-dom/global-registrator" :as happy-dom]
            [cards.core]))

(.register happy-dom/GlobalRegistrator)

(set! (.-node? js/window) true)
