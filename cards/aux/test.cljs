(ns aux.test
  (:require-macros [aux.test])
  #_{:clj-kondo/ignore [:unused-namespace]}
  (:require ["@testing-library/react" :as tlr]
            [promesa.core :as p]
            [reagent.core :as r]))

(def _wait-for tlr/waitFor)

(defn testing-container []
  (as-> (js/document.createElement "div") div
    (js/document.body.appendChild div)))

(defn cleanup
  ([] (tlr/cleanup))
  ([after-fn] (tlr/cleanup)
              (after-fn)))

(defn render
  [component]
  (tlr/render (r/as-element component)
              #js {:container (testing-container)}))

(defn text [el] (.-textContent el))

(defn length [el] (.-length el))

(defn get-class [^js/Element el]
  (.toString (.-classList el)))

(defn click
  [^js/Element el]
  (.click tlr/fireEvent el))

(defn qs
  ([^js/Element container query-data]
   (if (keyword? query-data)
     (.getAllByRole container (name query-data))
     (.getAllByText container query-data)))
  ([^js/Element container query-data filter-data]
   (.geAlltByRole container (name query-data) filter-data)))

(defn q
  ([^js/Element container query-data]
   (if (keyword? query-data)
     (.getByRole container (name query-data))
     (.getByText container query-data)))
  ([^js/Element container query-data filter-data]
   (.getByRole container (name query-data) filter-data)))

(defn q+
  ([^js/Element container query-data]
   (if (keyword? query-data)
     (.findByRole container (name query-data))
     (.findByText container query-data)))
  ([^js/Element container query-data query-filter]
   (.findByRole container (name query-data) query-filter)))

(defn qs+
  ([^js/Element container query-data]
   (if (keyword? query-data)
     (.findAllByRole container (name query-data))
     (.findAllByText container query-data)))
  ([^js/Element container query-data query-filter]
   (.findAllByRole container (name query-data) query-filter)))
