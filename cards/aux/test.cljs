(ns aux.test
  (:require ["@testing-library/react" :as tlr]
            ["@testing-library/user-event$default" :as ue]
            [reagent.core :as r]))

(def wait-for tlr/waitFor)

(defn testing-container []
  (as-> (js/document.createElement "div") div
    (js/document.body.appendChild div)))

(defn cleanup
  ([] (tlr/cleanup))
  ([& after-fns] (tlr/cleanup)
                 (doseq [f after-fns] (f))))

(defn render-container
  [component]
  (tlr/render (r/as-element component)
              #js {:container (testing-container)}))

(defn text [el] (.-textContent el))

(defn length [el] (.-length el))

(defn click [el] (.click el))

(defn type-in
  [el text]
  (ue/type el text))

(defn get-class [^js/Element el]
  (.toString (.-classList el)))

(defn- testing-library-fn [search-value opt]
  (str (if (:get opt) "get" "find")
       (when (:many? opt) "All")
       "By"
       (if (keyword? search-value) "Role" "Text")))

(defn- parse-search-value [search-value]
  (if (keyword? search-value)
    (name search-value)
    search-value))

(defn $
  "Search element(s) in the container by role or text. eg:

  Get a button → `{:get :button}`
  Get all rows → `{:get :row :many? true}`
  Find anything with text foo → `{:find #\"foo\"}`
  ```
  "
  [^js/Element container opt]
  (let [search-value (or (:get opt) (:find opt))
        testing-fn (aget container
                         (testing-library-fn search-value opt))]
    (.call testing-fn container (parse-search-value search-value))))

(defn render
  [component]
  (partial $ (render-container component)))
