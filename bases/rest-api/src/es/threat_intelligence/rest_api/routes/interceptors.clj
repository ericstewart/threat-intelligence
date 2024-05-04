(ns es.threat-intelligence.rest-api.routes.interceptors
  (:require [io.pedestal.http.content-negotiation :as content-negotiation]
            [clojure.data.json :as json]
            [es.threat-intelligence.log.interface :as log]
            [es.threat-intelligence.feed.interface :as feed]))

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
    "application/json" (json/write-str body)
    body))

(defn accepted-type
  "Determine the desired content-type based on the request accept
   header with a resonable default."
  [context]
  (get-in context [:request :accept :field] "application/json"))

(defn coerce-to
  "Coerce the response content and set the Content-Type header
   based on the accept header."
  [response content-type]
  (if (get-in response [:body])
    (-> response
        (update :body transform-content content-type)
        (assoc-in [:headers "Content-Type"] content-type))
    (-> response
        (assoc-in [:headers "Content-Type"] nil))))

;; Interceptor to coerce the response body to the desired conent type.
;;
;; Respects existing Content-Type header if already set, otherwise attempts
;; to determine the thype based on the accept header or default.
(def coerce-body-interceptor
  {:name ::coerce-body
   :leave (fn [context]
            (if (get-in context [:response :headers "Content-Type"])
              context
              (update-in context [:response] coerce-to (accepted-type context))))})

(def feed-interceptor
  {:name :feed-interceptor
   :enter (fn [context]
            (fn [context]
              (update context :request assoc :indicators feed/indicators)))})
