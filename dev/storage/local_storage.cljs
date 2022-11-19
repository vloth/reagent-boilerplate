(ns storage.local-storage)

(defn set!
  [item-name item-value]
  (.setItem (.-localStorage js/window) item-name item-value)
  item-value)

(defn get-item
  [item-name]
  (.getItem (.-localStorage js/window) item-name))

(defn del!
  [item-name]
  (.removeItem (.-localStorage js/window) item-name))
