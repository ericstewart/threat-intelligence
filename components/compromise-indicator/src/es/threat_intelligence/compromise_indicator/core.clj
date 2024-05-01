(ns es.threat-intelligence.compromise-indicator.core)

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (->> indicator-data
       (filter #(= (str (% "id")) (str indicator-id)))
       first))

(defn get-all
  "Return all indicators.

   If a type-filter parameter is given, return only indicators
   that match the specified type"
  ([indicator-data]
   (get-all indicator-data nil))
  ([indicator-data type-filter]
   (if type-filter
     (->> indicator-data
          (filter #(= (% "type") type-filter)))
     indicator-data)))
