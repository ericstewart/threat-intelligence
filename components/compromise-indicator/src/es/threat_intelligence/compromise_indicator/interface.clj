(ns es.threat-intelligence.compromise-indicator.interface
  (:require [es.threat-intelligence.compromise-indicator.core :as core]))

(defn filter-nonmatching-indicators
  [type-filter indicator-data]
  (core/filter-nonmatching-indicators type-filter indicator-data))

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (core/find-by-id indicator-data indicator-id))


(defn get-all
  "Return all indicators.

   If a type-filter parameter is given, return only indicators
   that match the specified type"
  ([indicator-data]
   (get-all indicator-data nil))
  ([indicator-data type-filter]
   (core/get-all indicator-data type-filter)))


(defn search
  "Search indicators based on a hash of one or more criteria"
  ([indicator-data]
   (search indicator-data {}))
  ([indicator-data search-criteria]
   (core/search indicator-data search-criteria)))
