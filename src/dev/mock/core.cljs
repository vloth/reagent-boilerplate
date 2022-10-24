(ns dev.mock.core
  (:require ["msw" :as msw]
            ["msw/node" :as msw-node]
            [dev.mock.config :refer [config]]
            [dev.mock.mount :refer [mount]]
            [dev.session-storage :as ss]
            [promesa.core :as p]))

(def ^:private ss-key "mock-active?")

(defonce mock-state (atom nil))

(defn node? [] (= true (.-node? js/window)))

(defn start-browser [handlers]
  (when-not (nil? @mock-state)
    (.resetHandlers ^js/Object @mock-state))
  (p/do (reset! mock-state handlers)
        (.start @mock-state #js {:onUnhandledRequest "bypass"})
        (ss/set-item! ss-key true)))

(defn stop-browser []
  (.stop @mock-state)
  (reset! mock-state nil)
  (ss/remove-item! ss-key))

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
  (if (ss/get-item ss-key)
    (start!)
    (js/Promise.resolve)))
