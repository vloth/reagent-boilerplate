(ns dev.mock.config
  (:require [app.adapters :refer [wallet-history->json]]
            [app.http-ports :as http]
            [app.schemas :refer [WalletHistory]]
            [malli.generator :as mg]))

(def config
  {(http/api "/wallet/history")
   {:get {:status 200
          :content-type :json
          :body (wallet-history->json
                 (mg/generate WalletHistory))}}

   (http/api "/wallet/deposit")
   {:post {:status 201}}
   
   (http/api "/wallet/withdrawal")
   {:post {:status 201}}})
