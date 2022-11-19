(ns dev-core
  (:require [app.core :as app]
            [malli.dev.cljs :as md]
            [mock.core :as mock]
            [promesa.core :as p]))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:dev/after-load refresh []
  (app/render))

(defn ^:export main []
  (p/do
    (dev-setup)
    (md/start!)
    (mock/init!)
    (app/render)))

(comment
  "Start mock service worker"
  (mock/start!))

(comment
  "Stop mock service worker"
  (mock/stop!))
