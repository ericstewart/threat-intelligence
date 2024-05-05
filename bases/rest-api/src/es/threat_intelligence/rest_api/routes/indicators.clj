(ns es.threat-intelligence.rest-api.routes.indicators
  (:require [io.pedestal.log :as log]
            [clojure.data.json :as json]
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
              (log/info :indicators-get {:type-filter type-filter :indicator-id indicator-id})
              (assoc context :response response)))})

(def indicator-search-interceptor
  {:name ::indicator-search-interceptor
   :enter (fn [context]
            (let [search-params (get-in context [:request :json-params])
                  indicator-db (feed/indicators)
                  indicators (compromise-indicator/search indicator-db search-params)
                  response (if indicators
                             (route-utils/ok indicators)
                             (route-utils/not-found nil))]
              (log/info :indicators-search {:search-params search-params})
              (assoc context :response response)))})
