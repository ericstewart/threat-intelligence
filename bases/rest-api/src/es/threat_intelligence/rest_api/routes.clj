(ns es.threat-intelligence.rest-api.routes
  (:require #_[io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [es.threat-intelligence.rest-api.routes.indicators :as indicator-routes]
            [es.threat-intelligence.rest-api.routes.interceptors :as interceptors]))


(def routes
  (route/expand-routes
    #{["/indicators"
       :get [interceptors/coerce-body-interceptor
             interceptors/content-negotiation-interceptor
             indicator-routes/get-indicators-interceptor]
       :route-name :indicators]}))
