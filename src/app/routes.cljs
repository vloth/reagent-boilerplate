(ns app.routes
  (:require [app.pages :as p]
            [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(defonce current-view (r/atom nil))

(def routes
  [["/"
    {:name :home
     :view #'p/home}]

   ["/deposit"
    {:name :deposit
     :view #'p/deposit}]

   ["/withdraw"
    {:name :withdraw
     :view #'p/withdraw}]

   ["/{*path}"
    {:name :not-found
     :view #'p/home}]])

(defn start-router! []
  (rfe/start!
   (rf/router routes {:conflicts nil})
   (fn [m] (reset! current-view (get-in m [:data :view])))
   {:use-fragment false}))
