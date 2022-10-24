(ns cards.user-story.wallet-journey-cards
  (:require [app.adapters :refer [wallet-history->json]]
            [app.http-ports :as http]
            [app.pages :as pages]
            [cards.test.aux :as t]
            [cards.wallet-history-cards :refer [a-wallet]]
            [cljs.test :refer [async is]]
            [dev.mock.core :as mock]
            [devcards.core :refer [defcard deftest]]
            [matcher-combinators.test :refer [match?]]
            [promesa.core :as p]))

(defcard "User wallet journey")

(set! devcards.core/test-timeout 2000)

(def wallet (a-wallet))

(def config
  {(http/api "/wallet/history")
   {:get {:status 200
          :content-type :json
          :body (wallet-history->json wallet)}}})

(deftest an-user-wallet-journey
  (async done
    (p/do (mock/start! config)
          (t/let-wait [container (t/render [:f> pages/home])
                       rows (t/qs+ container :row)]
                      (is (match?  (inc (count (:entries wallet))) ;inc because header +1
                                   (count rows)))
                      (t/cleanup)
                      (mock/stop!)
                      (done)))))
