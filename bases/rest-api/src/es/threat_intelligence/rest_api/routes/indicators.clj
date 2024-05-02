(ns es.threat-intelligence.rest-api.routes.indicators
  (:require [io.pedestal.log :as log]
            [es.threat-intelligence.rest-api.routes.utils :as route-utils]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]
            [es.threat-intelligence.feed.interface :as feed]))

(def get-indicators-interceptor
  {:name ::get-indicators
   :enter (fn [context]
            (let [indicator-id (get-in context [:request :path-params :id])
                  type-filter (get-in context [:request :query-params :type])
                  indicator-db (feed/indicators)
                  indicators (if indicator-id
                               (compromise-indicator/find-by-id indicator-db indicator-id)
                               (compromise-indicator/get-all indicator-db type-filter))
                  response (if indicators
                             (route-utils/ok indicators)
                             (route-utils/not-found nil))]
              (log/debug :indicators-get {:type-filter type-filter})
              (assoc context :response response)))})
