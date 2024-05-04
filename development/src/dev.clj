(ns dev
  (:require [es.threat-intelligence.rest-api.system :as rest-api-system]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :as crepl :refer [set-init start stop reset system]]))

#_(def ra-system (rest-api-system/new-system :dev))

(comment

  (reset)
  (set-init (fn [_old-system] (rest-api-system/new-system :dev)))
  (start)
  (stop)
  (keys system)
  :rcf)
