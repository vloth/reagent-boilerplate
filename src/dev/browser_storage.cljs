(ns dev.browser-storage)

(defn- set-item!
  [storage item-name item-value]
  (.setItem storage item-name item-value)
  item-value)

(defn- get-item
  [storage item-name]
  (.getItem storage item-name))

(defn- remove-item!
  [storage item-name]
  (.removeItem storage item-name))

(def session-get
  (partial get-item (.-sessionStorage js/window)))

(def session-set!
  (partial set-item! (.-sessionStorage js/window)))

(def session-del!
  (partial remove-item! (.-sessionStorage js/window)))

(def local-get
  (partial get-item (.-localStorage js/window)))

(def local-set!
  (partial set-item! (.-localStorage js/window)))

(def local-del!
  (partial remove-item! (.-localStorage js/window)))
