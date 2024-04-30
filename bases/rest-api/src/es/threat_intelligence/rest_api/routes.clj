(ns es.threat-intelligence.rest-api.routes
  (:require [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]))

(defn get-indicators
  [request]
  (let [indicator-db []
        indicators (compromise-indicator/get-all indicator-db)]
    {:status 200 :body "foo"}))

(def routes
  #{["/indicators" :get get-indicators :route-name :indicators]})
