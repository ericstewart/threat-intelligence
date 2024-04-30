
(ns es.threat-intelligence.rest-api.pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defrecord Pedestal [service-map service]
  component/Lifecycle
  (start [this]
    (if service
      this
      (assoc this :service
             (cond-> (http/create-server service-map)
               (not (test? service-map)) http/start))))
  (stop [this]
    (when (and service (not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))

(defn new-pedestal
  []
  (map->Pedestal {}))
