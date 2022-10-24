(ns app.adapters
  (:require [app.schemas :refer [WalletHistory]]
            [clojure.string :as str]
            [malli.core :as mc]
            [malli.transform :as mt]))

(defn json->str [json] (if json (.stringify js/JSON json) ""))

(defn str->json
  [string]
  (if-not (str/blank? string) (.parse js/JSON string) #js {}))

(defn str->float
  [string]
  (.parseFloat js/Number (str/replace string "," ".")))

(defn wallet-history->json
  [wallet]
  (clj->js (mc/encode WalletHistory wallet (mt/json-transformer))))

(defn json->wallet-history
  [json]
  (when (instance? js/Object json)
    (mc/decode WalletHistory
               (js->clj json :keywordize-keys true)
               (mt/json-transformer))))

(defn form->btc-value
  [form]
  (some-> (.get form "btc-amount")
          (str->float)))
