(ns deck.wallet-history-card
  (:require [app.components :as c]
            [app.schemas :refer [WalletEntry WalletHistory]]
            [aux.test :as t]
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
          $ (t/render [c/wallet-history (merge wallet {:entries [entry]})])]
      (is (match? (if (pos? btc-amount) "text-success" "text-danger")
                  (t/get-class ($ {:get (str (:btc-amount entry))}))))
      (t/cleanup)))

  (testing "lenght of rows should be entries + 1 (header)"
    (let [wallet (mg/generate WalletHistory)
          $ (t/render [c/wallet-history wallet])]
      (is (match? (inc (count (:entries wallet)))
                  (t/length ($ {:get :row :many? true}))))
      (t/cleanup)))

  (testing "empty state should show alert"
    (let [$ (t/render [c/wallet-history (a-wallet :empty)])]
      (is (match? "No entries found yet."
                  (t/text ($ {:get :alert}))))
      (t/cleanup))))
