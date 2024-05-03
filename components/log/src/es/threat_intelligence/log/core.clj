(ns es.threat-intelligence.log.core
  (:require [clojure.tools.logging :as log]))


(defn info
  [message & other-data]
  (log/info message other-data))

(defn debug
  [message & other-data]
  (log/debug message other-data))

(defn error
  [message & other-data]
  (log/error message other-data))
