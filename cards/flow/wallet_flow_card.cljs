(ns flow.wallet-flow-card
  (:require [app.adapters :refer [wallet-history->json]]
            [app.http-ports :as http]
            [app.pages :as pages]
            [aux.test :as t]
            [cljs.test :refer [async is]]
            [deck.wallet-history-card :refer [a-wallet]]
            [devcards.core :refer [defcard deftest]]
            [matcher-combinators.test :refer [match?]]
            [mock.core :as mock]
            [promesa.core :as p]))

(defcard "User wallet journey")

(def wallet (a-wallet :at-least-one))

(def config
  {(http/api "/wallet/history")
   {:get {:status 200
          :content-type :json
          :body (wallet-history->json wallet)}}

   (http/api "/wallet/withdrawal")
   {:post {:status 201}}

   (http/api "/wallet/deposit")
   {:post {:status 201}}})

(defn visualize-wallet-history []
  (p/let [$ (t/render [:f> pages/home])
          rows (t/wait-for #($ {:find :row :many? true}))]
    (is (match?  (inc (count (:entries wallet))) ;inc because header +1
                 (count rows))
        "wallet history visualized")))

(defn do-withdraw []
  (let [$ (t/render [pages/withdraw])]
    (p/do (t/type-in ($ {:get :spinbutton}) "100{enter}")
          (t/wait-for #($ {:find #"Created"}))
          (is true "withdraw created"))))

(defn do-deposit []
  (let [$ (t/render [pages/deposit])]
    (p/do
      (t/type-in ($ {:get :spinbutton}) "100{enter}")
      (t/wait-for #($ {:find #"Created"}))
      (is true "deposit created"))))

(deftest an-user-wallet-journey
  (async done
    (p/do (mock/start! config)
          (do-withdraw)
          (visualize-wallet-history)
          (do-deposit)
          (t/cleanup mock/stop! done))))
