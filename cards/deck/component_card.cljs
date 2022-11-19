(ns deck.component-card
  (:require [app.components :as c]
            [aux.test :as t]
            [cljs.test :refer [is testing]]
            [devcards.core :refer [defcard deftest] :as dc]
            [matcher-combinators.test :refer [match?]]))

(defcard simple-components
  "# Simple Components  
  Reusable components that can be simply called, without too much thought.")

(defcard hero
  (dc/reagent [c/hero]))

(defcard alerts
  (dc/reagent [:<>
               [c/alert :danger "This is a danger alert—check it out! (without an icon)"]
               [c/alert :bi-capslock :dark "This is a dark alert—check it out!"]
               [c/alert :bi-exclamation-diamond :warning "This is a warning alert—check it out!"]
               [c/alert :info "This is an info alert check it out! no icons too"]]))

(defcard thwarted-http
  (dc/reagent [:<>
               [c/thwarted-http {:status 404}]
               [c/thwarted-http {:body "an internal error occurred."}]]))

(defcard loading
  (dc/reagent [c/loading "custom loading text"]))

(defcard header
  (dc/reagent [c/header name]))

(deftest alert-test
  (testing "render info alert"
    (let [$ (t/render [c/alert :info "info"])]
      (is (match? "info"
                  (t/text ($ {:get :alert})))) 
      (t/cleanup)))

  (testing "render warning alert with icon"
    (let [$ (t/render [c/alert :bi-exclamation-diamond :warning "warn"])]
      (is (match? #"bi-exclamation-diamond"
                  (t/get-class ($ {:get :img}))))
      (t/cleanup))))

(deftest thwarted-test
  (testing "thwarted unreachable network"
    (let [$ (t/render [c/thwarted-http {:status 404}])]
      (is (match? #"Could not reach server."
                  (t/text ($ {:get :alert})))) 
      (t/cleanup)))

  (testing "thwarted unknown error"
    (let [$ (t/render [c/thwarted-http {:body "Internal error"}])]
      (is (match? #"Internal error"
                  (t/text ($ {:get :alert}))))
      (t/cleanup))))

(deftest hader
  (testing "render nav items"
    (let [$ (t/render [c/header name])]
      (is (match? ["Home" "Deposit" "Withdraw"]
                  (mapv t/text ($ {:get :listitem :many? true}))))
      (t/cleanup))))

