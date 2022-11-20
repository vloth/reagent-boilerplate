(ns app.components
  (:require [app.lib :refer [use-state]]
            [app.schemas :refer [WalletHistory]]
            [clojure.string :as str]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]))

(defn alert
  ([kind text] [alert nil kind text])
  ([icon kind text]
   [:div.alert.d-flex.align-items-center
    {:role "alert" :className (str "alert-" (name kind))}
    (when icon [:i {:role "img" :className (str "px-2 bi " (name icon))}]) text]))

(defn thwarted-http
  [miss]
  (if (= 404 (:status miss))
    [alert :bi-wifi-off :warning
     "Could not reach server. Please try again later."]
    [alert :bi-dash-circle-dotted :danger (str "Error: " (:body miss))]))

(defn loading [text]
  [:<> [:h4.fw-lighter.text-center.text-muted text]
   [:p.placeholder.placeholder-glow.col-12.placeholder-lg]
   [:p.placeholder.placeholder-glow.col-12.placeholder-lg]
   [:p.placeholder.placeholder-glow.col-12.placeholder-lg]])

(defn header
  ([] (header rfe/href))
  ([href]
   (r/with-let
     [items {:home "Home" :deposit "Deposit" :withdraw "Withdraw"}]
     [:header.p-3>div.container
      [:div.d-flex.flex-wrap.align-items-center.justify-content-center.justify-content-lg-start
       [:a.d-flex.align-items-center.mb-2.mb-lg-0.text-white.text-decoration-none
        [:i.bi-x-diamond.text-dark]]
       [:ul.nav.col-12.col-lg-auto.me-lg-auto.mb-2.justify-content-center.mb-md-0
        (for [[view-name label] items]
          ^{:key label}
          [:li>a.nav-link.px-2.text-secondary
           {:style {:cursor "pointer"} :href (href view-name)}
           label])]]])))

(defn hero []
  [:div.p-5.mb-4.rounded-3.text-bg-dark
   [:div.container-fluid.py-5
    [:h1.display-5.fw-bold "Hello from reagent-boilerplate"]
    [:p.col-md-8.fs-4
     "This is a complete example, with router, testing and fetch. 🎉"]]])

;; ?
(defn form-data [thing]
  (if (.-FormData js/window)
    (new js/FormData thing)
    (let [form-state (atom thing)
          f #js {}]
      (set! (.-append f) #(swap! form-state assoc %1 %2))
      (set! (.-get f) #(clj->js (get @form-state %)))
      f)))

(defn transaction-form
  [kind text on-submit]
  (let [[amount set-amount] (use-state "")
        on-submit (fn [event]
                    (.preventDefault event)
                    (on-submit (form-data (.-target event))))]
    [:form {:on-submit on-submit}
     [:div.form-group.mb-2 [:label {:for "btc-amount"} "amount of btc"]
      [:input.form-control
       {:id        "btc-amount"
        :name      "btc-amount"
        :type      "number"
        :step      "0.01"
        :on-change (fn [e]
                     (set-amount (.. e -target -value)))}]]
     [:button.btn
      {:disabled (str/blank? amount) :className (str "btn-" (name kind))}
      text]]))

(defn wallet-history
  {:malli/schema [:=> [:cat WalletHistory] [:vector any?]]}
  [{:keys [entries]}]
  (if-not (seq entries)
    [:div.alert.alert-secondary {:role "alert"}
     [:i.bi.bi-eye-slash.px-2] "No entries found yet."]
    [:table.table.table-striped.table-borderless.table-sm
     [:thead>tr [:th {:scope "column"} "Id"] [:th {:scope "column"} "BTC"]
      [:th {:scope "column"} "US dollars"] [:th {:scope "column"} "Date"]]
     [:tbody
      (for [{:keys [id btc-amount usd-amount-at created-at]} entries]
        ^{:key (.toString id)}
        [:tr [:th (.toString id)]
         [:th {:style {:white-space "nowrap"}}
          (if (pos? btc-amount)
            [:span {:className "text-success"} [:i.bi.bi-arrow-up-short] btc-amount]
            [:span {:className "text-danger"} [:i.bi.bi-arrow-down-short]
             btc-amount])] [:th usd-amount-at]
         [:th (.toLocaleDateString created-at "en-US")]])]]))
