(ns app.pages
  (:require [app.adapters :refer [form->btc-value json->wallet-history]]
            [app.components :as c]
            [app.http-ports :as http-ports]
            [app.lib :refer [use-state use-effect]]
            [promesa.core :as p]))

; (def home-state (r/atom {:loading? false :data nil}))
; (defn home []
;   (swap! home-state assoc :loading? true)
;   (p/->> (http-ports/wallet-history)
;          (swap! home-state assoc :loading? false :data))
;   (fn []
;     [:div [:h1.text-center.fw-lighter "Wallet history"] [:hr]
;      (if-let [history (-> @home-state :data :body json->wallet-history)]
;        [c/wallet-history history]
;        (if (:loading? @home-state)
;          [c/loading "loading your wallet..."]
;          [c/thwarted-http (:data @home-state)]))]))

; Below the equivalent using react hooks
(defn home []
  (let [[state set-state] (use-state {:loading? true :data nil})
        _ (use-effect #(p/->> (http-ports/wallet-history)
                              (assoc {:loading? false} :data)
                              (set-state)))]
    [:div [:h1.text-center.fw-lighter "Wallet history"] [:hr]
     (if-let [history (-> state :data :body json->wallet-history)]
       [c/wallet-history history]
       (if (:loading? state)
         [c/loading "loading your wallet..."]
         [c/thwarted-http (:data state)]))]))

(defn- transaction-page
  [kind label http-port-fn]
  (let [[state set-state] (use-state :idle)
        on-submit         #(do (set-state :sending)
                               (p/-> (http-port-fn (form->btc-value %))
                                     ((fn [{:keys [status]}]
                                        (if (= 201 status) :created :error)))
                                     (set-state)))]
    [:div.container
     [:div.row.justify-content-md-center [:h1.text-center.fw-lighter label]
      [:hr] [:div.col-md-4.mb-2 [:f> c/transaction-form kind label on-submit]]]
     [:div.row.justify-content-md-center>div.col-md-6
      (case state
        :sending [c/alert :primary "Sending"]
        :created [c/alert :success "Created"]
        :error [c/alert :danger "Error"]
        nil)]]))

(defn deposit []
  [:f> transaction-page :success "Deposit" http-ports/deposit])

(defn withdraw  []
  [:f> transaction-page :dark "Withdraw" http-ports/withdraw])
