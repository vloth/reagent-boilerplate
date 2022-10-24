(ns cards.wallet-history-cards
  (:require [app.components :as c]
            [app.schemas :refer [WalletEntry WalletHistory]]
            [cards.test.aux :as t]
            [cljs.test :refer [is testing]]
            [devcards.core :as dc :refer [defcard defcard-doc deftest]]
            [malli.generator :as mg]
            [malli.util :as mu]
            [matcher-combinators.test :refer [match?]]))

(defn a-wallet
  ([kind]
   (->> (case kind
          :empty        {:gen/elements [[]]}
          :at-least-one {:gen/min 1}
          :single-entry {:gen/min 1 :gen/max 1}
          nil)
        ((fn [attr] [:map [:entries [:vector attr WalletEntry]]]))
        (mu/merge WalletHistory)
        (mg/generate)))
  ([] (a-wallet nil)))

(defcard with-entries
  (dc/reagent [c/wallet-history (a-wallet :at-least-one)]))

(defcard-doc
  "# Wallet History
   Shows the history of a wallet in a table format.
   When there are no entries in the wallet, an empty state is shown instead.
   Here is the schema for wallet history:"
  WalletHistory)

(defcard with-empty
  (dc/reagent [c/wallet-history (a-wallet :empty)]))

(deftest wallet-history-tests
  (testing "stylized btc-amount based on positivity"
    (let [wallet (a-wallet :single-entry)
          entry (first (:entries wallet))
          btc-amount (:btc-amount entry)
          container (t/render [c/wallet-history (merge wallet {:entries [entry]})])]
      (is (match? (if (pos? btc-amount) "text-success" "text-danger")
                  (-> (t/q container (:btc-amount entry))
                      t/get-class)))
      (t/cleanup)))

  (testing "lenght of rows should be entries + 1 (header)"
    (let [wallet (mg/generate WalletHistory)
          container (t/render [c/wallet-history wallet])]
      (is (match? (inc (count (:entries wallet)))
                  (-> (t/qs container :row)
                      t/length)))
      (t/cleanup)))

  (testing "empty state should show alert"
    (let [container (t/render [c/wallet-history (a-wallet :empty)])]
      (is (match? "No entries found yet."
                  (-> (t/q container :alert)
                      t/text)))
      (t/cleanup))))
