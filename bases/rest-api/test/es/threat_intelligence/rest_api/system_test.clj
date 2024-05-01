(ns es.threat-intelligence.rest-api.system-test
  (:require [es.threat-intelligence.rest-api.system :as sut]
            [es.threat-intelligence.rest-api.routes :as routes]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]
            [es.threat-intelligence.feed.interface :as feed]
            [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [clojure.data.json :as json]
            [clojure.test :as test :refer :all]))

(def minimal-indicator-data [{"id" 12345} {"id" 12346} {"id" 41234}])

;;---------------------------------------------------------------
;; Technique borrowed from clojure-polylith-relworld-example-app
;; as a way to stub data for the system under test
(defn stub-components
  [f]
  (with-redefs [feed/indicators (fn [] minimal-indicator-data)]
    (f)))

(use-fixtures :each stub-components)
;;----------------------------------------------------------------

;;----------------------------------------------------------------
;; Borrowed from Pedestal docs on testing
(def url-for (route/url-for-routes
               routes/routes))

(defn service-fn
  [system]
  (get-in system [:pedestal :service ::http/service-fn]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally (component/stop ~bound-var)))))
;;----------------------------------------------------------------

(deftest indicators-test
  (with-system [sut (sut/new-system :test)]
    (testing "GET on /indicators with default headers"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response} (response-for service :get (url-for :indicators))
            body-map (json/read-str body)]
        (is (= 200 status) "should be a successful response")
        (is (= "application/json" (headers "Content-Type")) "should be a json response")
        (is (= minimal-indicator-data body-map) "body should contain expected records")))

    (testing "GET on /indicators with Accept header set to an invalid type"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response} (response-for service :get (url-for :indicators) :headers {"Accept" "text/plain"})]
        (is (= 406 status) "should be a not acceptable response")
        (is (= "application/json" (headers "Content-Type")) "should be JSON")
        (is (= "\"Not Acceptable\"" body) "body should indicate 'Not Acceptable'")))))
