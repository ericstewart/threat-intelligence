(ns es.threat-intelligence.rest-api.routes
  (:require #_[io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [es.threat-intelligence.rest-api.routes.indicators :as indicator-routes]
            [es.threat-intelligence.rest-api.routes.interceptors :as interceptors]))

(def numeric #"[0-9]+")
(def indicator-id {:id numeric})

(def routes
  (route/expand-routes
    #{["/indicators" :get [interceptors/coerce-body-interceptor
                           interceptors/content-negotiation-interceptor
                           indicator-routes/get-indicators-interceptor]
       :route-name :indicators]
      ["/indicators/search" :post [interceptors/coerce-body-interceptor
                                   interceptors/content-negotiation-interceptor
                                   (body-params/body-params)
                                   indicator-routes/indicator-search-interceptor
                                   http/json-body]
       :route-name :indicators-search]
      ["/indicators/:id" :get [
                               interceptors/content-negotiation-interceptor
                               indicator-routes/get-indicators-interceptor
                               http/json-body]
       :route-name :indicators-item-view]}))
