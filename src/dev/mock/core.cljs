(ns dev.mock.core
  (:require ["msw" :as msw]
            ["msw/node" :as msw-node]
            [dev.browser-storage :as s]
            [dev.mock.config :refer [config]]
            [dev.mock.mount :refer [mount]]
            [promesa.core :as p]))

(def ^:private mock-active-key "mock-active?")

(defonce mock-state (atom nil))

(defn node? [] (= true (.-node? js/window)))

(defn start-browser [handlers]
  (when-not (nil? @mock-state)
    (.resetHandlers ^js/Object @mock-state))
  (s/local-set! mock-active-key true)
  (.start (reset! mock-state handlers)
          #js {:onUnhandledRequest "bypass"}))

(defn stop-browser []
  (.stop @mock-state)
  (reset! mock-state nil)
  (s/local-del! mock-active-key))

(defn start-node [handlers]
  (p/do (reset! mock-state handlers)
        (.listen ^js/Object @mock-state)))

(defn stop-node []
  (p/do (.close ^js/Object @mock-state)
        (reset! mock-state nil)))

(defn start!
  ([] (start! config))
  ([custom-config]
   (let [[start-fn setup-fn]
         (if (node?) [start-node msw-node/setupServer]
             [start-browser msw/setupWorker])]
     (start-fn (apply setup-fn (mount custom-config))))))

(defn stop! []
  (if (node?) (stop-node) (stop-browser)))

(defn init! []
  (if (s/local-get mock-active-key)
    (start!)
    (js/Promise.resolve)))
