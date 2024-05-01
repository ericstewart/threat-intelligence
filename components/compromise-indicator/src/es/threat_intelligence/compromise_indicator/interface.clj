(ns es.threat-intelligence.compromise-indicator.interface
  (:require [es.threat-intelligence.compromise-indicator.core :as core]))

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (core/find-by-id indicator-data indicator-id))


(defn get-all
  [indicator-data]
  (core/get-all indicator-data))
