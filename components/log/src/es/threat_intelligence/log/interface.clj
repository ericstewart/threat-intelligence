(ns es.threat-intelligence.log.interface
  (:require [es.threat-intelligence.log.core :as core]))


(defn info
  "A info level log message"
  [message & other-data]
  (core/info message other-data))

(defn debug
  "A debug level log message"
  [message & other-data]
  (core/debug message other-data))

(defn error
  "An error level log message"
  [message & other-data]
  (core/error message other-data))
