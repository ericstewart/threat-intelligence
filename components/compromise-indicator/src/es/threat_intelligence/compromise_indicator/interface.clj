(ns es.threat-intelligence.compromise-indicator.interface
  (:require [es.threat-intelligence.compromise-indicator.core :as core]))

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-db indicator-id]
  (core/find-by-id indicator-db indicator-id))
