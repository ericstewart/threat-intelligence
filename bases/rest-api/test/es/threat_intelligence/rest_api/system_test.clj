(ns es.threat-intelligence.rest-api.system-test
  (:require [es.threat-intelligence.rest-api.system :as sut]
            [es.threat-intelligence.rest-api.routes :as routes]
            [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [clojure.test :as test :refer :all]))




(def url-for (route/url-for-routes
               (route/expand-routes routes/routes)))

(defn service-fn
  [system]
  (get-in system [:pedestal :service ::http/service-fn]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally (component/stop ~bound-var)))))

(deftest indicators-test
  (with-system [sut (sut/new-system :test)]
    (let [service (service-fn sut)
          {:keys [status body]} (response-for service :get (url-for :indicators))]
      (is (= 200 status)))))
