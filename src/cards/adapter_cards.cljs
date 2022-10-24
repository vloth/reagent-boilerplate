(ns cards.adapter-cards
  (:require [app.adapters :as adapters]
            [cards.wallet-history-cards :refer [a-wallet]]
            [cljs.test :refer [are is testing]]
            [devcards.core :as dc :refer [defcard-doc deftest]]
            [matcher-combinators.test :refer [match?]]))

(defcard-doc
  "# Adapters
   pure functions to map between types.")

(defn form-data []
  (if (.-FormData js/window)
    (new js/FormData)
    (let [form-state (atom {})
          f #js {}]
      (set! (.-append f) #(swap! form-state assoc %1 %2))
      (set! (.-get f) #(clj->js (get @form-state %)))
      f)))

(deftest adapters
  (testing "json->string"
    (are [x y] (= x (adapters/json->str y))
      "{\"foo\":1}" #js {:foo 1}
      "{}"          #js {}
      "[]"          #js []
      ""            nil
      ""            js/undefined))

  (testing "str->json"
    (are [x y] (match? (js->clj x) (js->clj (adapters/str->json y)))
      #js {:foo 1} "{\"foo\":1}"
      #js {}       "{}"
      #js []       "[]"
      #js {}       ""))

  (testing "str->float"
    (is (every? NaN? (map adapters/str->float ["" "garbage"])))
    (are [x y] (match? x (adapters/str->float y))
      123.45 "123.45"
      123.45 "123,45"
      123    "123"))

  ; happy-dom does not support form data :thinking:
  (testing "form->btc-value"
    (let [form (form-data)]
      (.append form "btc-amount" "123,45")
      (is (match? 123.45 (adapters/form->btc-value form))))

    (let [form (form-data)]
        (.append form "btc-value" "123,45")
        (is (match? nil (adapters/form->btc-value form)))))

  (testing "wallet-history->json and json->wallet-history associativity"
    (let [wallet (a-wallet)
          json-wallet (adapters/wallet-history->json wallet)
          from-js-land (adapters/json->wallet-history json-wallet)]
      (is (match? (count (:entries wallet))
                  (.. json-wallet -entries -length)))
      (is (match? wallet from-js-land)))))

