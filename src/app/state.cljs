(ns app.state
  (:require [reagent.core :as r]))

(defonce state
  (r/atom {:home/loading?  false
           :home/wallet     nil
           :deposit/state   nil
           :withdraw/state  nil}))
