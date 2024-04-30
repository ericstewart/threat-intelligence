(ns es.threat-intelligence.rest-api.system
  (:require [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [io.pedestal.http :as http]
            [es.threat-intelligence.rest-api.pedestal :as pedestal]
            [es.threat-intelligence.rest-api.routes :as routes]))

(defn new-system
  [env]
  (component/system-map
    :service-map {:env env
                  ::http/routes routes/routes
                  ::http/type :jetty
                  ::http/port 8890
                  ::http/join? false}
    :pedestal (component/using
                (pedestal/new-pedestal) [:service-map])))

(set-init (fn [old-system] (new-system :prod)))
