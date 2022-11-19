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
          :body (wallet-history->json wallet)}}})

(deftest an-user-wallet-journey
  (async done
    (p/do (mock/start! config)
          (p/let [$ (t/render [:f> pages/home])
                  rows (t/wait-for #($ {:find :row :many? true}))]
            (is (match?  (inc (count (:entries wallet))) ;inc because header +1
                         (count rows)))
            (t/cleanup mock/stop! done)))))
