(ns app.pages
  (:require [app.adapters :refer [form->btc-value json->wallet-history]]
            [app.components :as c]
            [app.http-ports :as http-ports]
            [app.state :refer [state]]
            [promesa.core :as p]))

(defn home []
  (swap! state assoc :home/wallet nil :home/loading? true)
  (p/->> (http-ports/wallet-history)
         (swap! state assoc :home/loading? false :home/wallet))
  (fn []
    [:div [:h1.text-center.fw-lighter "Wallet history"] [:hr]]
    (if (:home/loading? @state)
      [c/loading "loading your wallet..."]
      (if-let [history (-> @state :home/wallet :body json->wallet-history)]
        [c/wallet-history history]
        [c/thwarted-http (:home/wallet @state)]))))

(defn- transaction-page
  [kind label transaction-state http-port-fn]
  (swap! state assoc transaction-state nil)
  (let [on-submit
        (fn [e] (swap! state assoc transaction-state :sending)
          (p/->> (http-port-fn (form->btc-value e))
                 ((fn [{:keys [status]}]
                    (if (= 201 status) :created :error)))
                 (swap! state assoc transaction-state)))]
    (fn []
      [:div.container
       [:div.row.justify-content-md-center [:h1.text-center.fw-lighter label]
        [:hr] [:div.col-md-4.mb-2 [:f> c/transaction-form kind label on-submit]]]
       [:div.row.justify-content-md-center>div.col-md-6
        (case (get @state transaction-state)
          :sending [c/alert :primary "Sending"]
          :created [c/alert :success "Created"]
          :error [c/alert :danger "Error"]
          nil)]])))

(defn deposit []
  [transaction-page :success "Deposit" :deposit/state http-ports/deposit])

(defn withdraw  []
  [transaction-page :dark "Withdraw" :withdraw/state http-ports/withdraw])
