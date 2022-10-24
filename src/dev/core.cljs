(ns dev.core
  (:require [app.core :as app]
            [dev.mock.core :as mock]
            [malli.dev.cljs :as md]
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

(comment "Start mock service worker"
         (p/do (mock/start!)
               (.reload js/location)))

(comment "Stop mock service worker"
         (p/do (mock/stop!)
               (.reload js/location)))
