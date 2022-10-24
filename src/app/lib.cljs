(ns app.lib
  (:require ["react" :as react]))

(defn use-effect
  ([deps f] (react/useEffect (fn [] (f) js/undefined) deps))
  ([f] (use-effect #js [] f)))

(defn use-state
  [initial-state]
  (react/useState initial-state))
