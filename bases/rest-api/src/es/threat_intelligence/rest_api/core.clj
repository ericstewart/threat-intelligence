(ns es.threat-intelligence.rest-api.core
  (:require [com.stuartsierra.component :as component]
            [es.threat-intelligence.rest-api.system :as system])
  (:gen-class))

(defn -main
  [& _args]
  (component/start (system/new-system :prod)))
