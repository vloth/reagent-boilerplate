(ns cards.test.matcher-setup
  (:require [cljs.test :as t]
            [matcher-combinators.cljs-test]
            [matcher-combinators.printer]
            [matcher-combinators.test]))

(defn- collect-test [m]
  (cljs.test/update-current-env!
   [:_devcards_collect_tests] conj
   (merge (select-keys (cljs.test/get-current-env) [:testing-contexts]) m)))

(defmethod cljs.test/report
  [:_devcards_test_card_reporter :matcher-combinators/mismatch]
  [m]
  (t/inc-report-counter! :fail)
  (collect-test {:type :fail
                 :message "FAIL: mismatch"
                 :actual (:actual m)}))
