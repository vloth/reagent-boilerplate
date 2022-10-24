(ns app.http-ports
  (:require [lambdaisland.fetch :as fetch]))

(goog-define API_HOST "http://localhost")

(defn api [path] (str API_HOST path))

(defn wallet-history []
  (fetch/get (api "/wallet/history")))

(defn deposit
  [btc-amount]
  (fetch/post (api "/wallet/deposit")
              {:content-type :json
               :body {:btc-amount btc-amount}}))

(defn withdraw
  [btc-amount]
  (fetch/post (api "/wallet/withdrawal")
              {:content-type :json
               :body {:btc-amount btc-amount}}))
