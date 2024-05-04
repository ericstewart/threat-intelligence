(ns es.threat-intelligence.compromise-indicator.core
  (:require [clojure.data :as data]
            [es.threat-intelligence.log.interface :as log]))

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (do
    (log/info "find-by-id called with id:" indicator-id)
    (->> indicator-data
         (filter #(= (str (% "id")) (str indicator-id)))
         first)))

(defn get-all
  "Return all indicators.

   If a type-filter parameter is given, return only indicators
   that match the specified type"
  ([indicator-data]
   (get-all indicator-data nil))
  ([indicator-data type-filter]
   (do
     (log/info "get-all called with type-filter: " type-filter)
     (if type-filter
       (->> indicator-data
            (filter #(= (% "type") type-filter)))
       indicator-data))))

(defn search
  "Search indicators based on a hash of one or more criteria"
  [indicator-data search-criteria]
  (let [modified-search (clojure.walk/stringify-keys search-criteria)]
    (do
      (log/info "search called with criteria for :" (str (keys modified-search)))
      (filter (fn [indicator]
                (let [[_ _ same] (clojure.data/diff indicator modified-search)]
                  (not (empty? same))))
              indicator-data))))

(comment
  (get-all [{"type" "FileHash-SHA256"}] "FileHash-SHA256"))
