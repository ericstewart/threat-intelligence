(ns es.threat-intelligence.compromise-indicator.core)

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-db indicator-id]
  (first (filter #(= (% "id") indicator-id) indicator-db)))
