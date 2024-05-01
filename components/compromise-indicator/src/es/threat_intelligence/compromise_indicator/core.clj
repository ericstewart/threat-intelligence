(ns es.threat-intelligence.compromise-indicator.core)

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (first (filter #(= (% "id") indicator-id) indicator-data)))

(defn get-all
  [indicator-data]
  indicator-data)
