(ns es.threat-intelligence.rest-api.routes.indicators
  (:require [es.threat-intelligence.rest-api.routes.utils :as route-utils]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]
            [es.threat-intelligence.feed.interface :as feed]))

(def get-indicators-interceptor
  {:name ::get-indicators
   :enter (fn [context]
            (let [request (:request context)
                  indicator-db (feed/indicators)
                  indicators (compromise-indicator/get-all indicator-db)
                  response (route-utils/ok indicators)]
              (assoc context :response response)))})
