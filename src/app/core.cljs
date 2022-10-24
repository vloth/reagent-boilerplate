(ns app.core
  (:require [app.components :as c]
            [app.routes :refer [current-view start-router!]]
            [reagent.dom :as r]))

(defn layout []
  [:<> [c/header] [c/hero]
   (when @current-view
     [:div.container>div.row.justify-content-md-center>div.col-lg-8
      [:f> @current-view]])])

(defn render []
  (start-router!)
  (r/render [layout]
            (.getElementById js/document "app")))

(defn ^:export main [] (render))
