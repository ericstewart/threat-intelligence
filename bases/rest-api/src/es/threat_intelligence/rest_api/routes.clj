(ns es.threat-intelligence.rest-api.routes
  (:require [clojure.data.json :as json]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.content-negotiation :as content-negotiation]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]
            [es.threat-intelligence.feed.interface :as feed]))

#_(defn get-indicators
    [request]
    (let [indicator-db (feed/indicators)
          indicators (compromise-indicator/get-all indicator-db)]
      {:status 200
       :body indicators}))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))

(def get-indicators-interceptor
  {:name ::get-indicators
   :enter (fn [context]
            (let [request (:request context)
                  indicator-db (feed/indicators)
                  indicators (compromise-indicator/get-all indicator-db)
                  response (ok indicators)]
              (assoc context :response response)))})

;; Limit which content types this api supports
(def supported-types ["application/json"])

(def content-negotiation-interceptor
  (content-negotiation/negotiate-content supported-types))

(defn transform-content
  "Transform the body based on the specified Content-Type"
  [body content-type]
  (case content-type
    "text/html" body
    "text/plain" body
    "application/json" (json/write-str body)))

(defn accepted-type
  "Determine the desired content-type based on the request accept
   header with a resonable default."
  [context]
  (get-in context [:request :accept :field] "application/json"))

(defn coerce-to
  "Coerce the response content and set the Content-Type header
   based on the accept header."
  [response content-type]
  (-> response
      (update :body transform-content content-type)
      (assoc-in [:headers "Content-Type"] content-type)))

;; Interceptor to coerce the response body to the desired ctonent type.
;;
;; Respects existing Content-Type header if already set, otherwise attempts
;; to determine the thype based on the accept header or default.
(def coerce-body-interceptor
  {:name ::coerce-body
   :leave (fn [context]
            (if (get-in context [:response :headers "Content-Type"])
              context
              (update-in context [:response] coerce-to (accepted-type context))))})

(def routes
  (route/expand-routes
    #{["/indicators"
       :get [coerce-body-interceptor
             content-negotiation-interceptor
             get-indicators-interceptor]
       :route-name :indicators]}))
