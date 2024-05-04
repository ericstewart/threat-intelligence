(ns user
  (:require [com.stuartsierra.component :as component]
            [com.stuartsierra.component.user-helpers :refer [go dev reset set-dev-ns]]))

(comment
  (set-dev-ns 'es.threat-intelligence.rest-api.system)
  (go)
  (dev)
  (reset)
  :rcf)
