(ns app.schemas)

(def WalletEntry
  [:map
   [:id uuid?]
   [:btc-amount [:double {:gen/infinite? false :gen/NaN? false}]]
   [:usd-amount-at [pos? {:gen/infinite? false :gen/NaN? false}]]
   [:created-at inst?]])

(def WalletHistory
  [:map
   [:entries [:vector WalletEntry]]
   [:total-btc [:double {:gen/infinite? false :gen/NaN? false}]]
   [:total-current-usd [:double {:gen/infinite? false :gen/NaN? false}]]])
