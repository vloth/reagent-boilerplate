(ns mock.core
  (:require ["msw" :as msw]
            ["msw/node" :as msw-node]
            [mock.config :refer [config]]
            [mock.mount :refer [mount]]
            [promesa.core :as p]
            [storage.local-storage :as s]))

(def ^:private storage-key "mock-active?")

(defonce mock-state (atom nil))

(defn node? [] (= true (.-node? js/window)))

(defn start-browser [handlers]
  (when-not (nil? @mock-state)
    (.resetHandlers ^js/Object @mock-state))
  (s/set! storage-key true)
  (.start (reset! mock-state handlers)
          #js {:onUnhandledRequest "bypass"}))

(defn stop-browser []
  (.stop @mock-state)
  (reset! mock-state nil)
  (s/del! storage-key))

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
  (if (s/get-item storage-key)
    (start!)
    (js/Promise.resolve)))
