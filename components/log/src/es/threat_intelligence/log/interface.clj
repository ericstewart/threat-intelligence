(ns es.threat-intelligence.log.interface
  (:require [es.threat-intelligence.log.core :as core]))


(defn info
  [message & other-data]
  (core/info message other-data))

(defn debug
  [message & other-data]
  (core/debug message other-data))

(defn error
  [message & other-data]
  (core/error message other-data))
