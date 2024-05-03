(ns es.threat-intelligence.rest-api.routes.indicators-test
  (:require [es.threat-intelligence.rest-api.routes.indicators :as sut]
            [clojure.test :as t :refer :all]))

(def minimal-indicator-data [{"id" 12345 "type" "IPv4"} {"id" 12346 "type" "FileHash-SHA256"} {"id" 41234 "type" "IPv4"}])

;; ;;---------------------------------------------------------------
;; ;; Technique borrowed from clojure-polylith-relworld-example-app
;; ;; as a way to stub data for the system under test
;; (defn stub-components
;;   [f]
;;   (with-redefs [feed/indicators (fn [] minimal-indicator-data)]
;;     (f)))

;; (use-fixtures :each stub-components)
;; ;;----------------------------------------------------------------

;; ;;----------------------------------------------------------------
;; ;; Borrowed from Pedestal docs on testing
;; (def url-for (route/url-for-routes routes/routes))

;; (defn service-fn
;;   [system]
;;   (get-in system [:pedestal :service ::http/service-fn]))

;; (defmacro with-system
;;   [[bound-var binding-expr] & body]
;;   `(let [~bound-var (component/start ~binding-expr)]
;;      (try
;;        ~@body
;;        (finally (component/stop ~bound-var)))))
;;----------------------------------------------------------------

(deftest indicators-test
  (testing "retrieve all indicators"
    (let [context {:indicators minimal-indicator-data :request {}}
          post-context ((:enter sut/get-indicators-interceptor) context)
          {:keys [response]} post-context]
      (is (= 200 (:status response))
          (= minimal-indicator-data (:body response))))))

;; (deftest indicators-test
;;   (with-system [sut (sut/new-system :test)]
;;     (testing "GET on /indicators with default headers"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response} (response-for service :get (url-for :indicators))
;;             body-json (json/read-str body)]
;;         (is (= 200 status) "should be a successful response")
;;         (is (= "application/json" (headers "Content-Type")) "should be a json response")
;;         (is (= minimal-indicator-data body-json) "body should contain expected records")))

;;     (testing "GET on /indicators with Accept header set to an invalid type"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response} (response-for service :get (url-for :indicators) :headers {"Accept" "text/plain"})]
;;         (is (= 406 status) "should be a not acceptable response")
;;         (is (= "application/json" (headers "Content-Type")) "should be JSON")
;;         (is (= "\"Not Acceptable\"" body) "body should indicate 'Not Acceptable'")))

;;     (testing "GET on /indicators with type filtering"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response}
;;             (response-for service :get (url-for :indicators :query-params {:type "IPv4"}))
;;             body-json (json/read-str body)]
;;         (is (= 200 status) "should be a successful response")
;;         (is (= "application/json" (headers "Content-Type")) "should be a json response")
;;         (is (= 2 (count body-json)) "body should contain expected records")
;;         (is (= [{"id" 12345 "type" "IPv4"} {"id" 41234 "type" "IPv4"}] body-json) "body should contain expected records")))

;;     (testing "GET on /indicators with type filtering (type with escaped characters)"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response}
;;             (response-for service :get (url-for :indicators :query-params {:type "FileHash-SHA256"}))
;;             body-json (json/read-str body)]

;;         (is (= 200 status) "should be a successful response")
;;         (is (= "application/json" (headers "Content-Type")) "should be a json response")
;;         (is (= 1 (count body-json)) "body should contain expected records")
;;         (is (= [{"id" 12346 "type" "FileHash-SHA256"}] body-json) "body should contain expected records")))))

;; (deftest get-single-indicator-test
;;   (with-system [sut (sut/new-system :test)]

;;     (testing "GET on /indicators/:id"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response}
;;             (response-for service :get (url-for :indicators-item-view :path-params {:id 12346}))
;;             body-json (json/read-str body)]

;;         (is (= 200 status) "should be a successful response")
;;         (is (= "application/json" (headers "Content-Type")) "should be a json response")
;;         (is (= {"id" 12346 "type" "FileHash-SHA256"} body-json) "body should contain the specific record")))

;;     (testing "GET on /indicators/:id with id that doesn't exist"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response}
;;             (response-for service :get (url-for :indicators-item-view :path-params {:id 99999}))
;;             body-json (json/read-str body)]

;;         (is (= 404 status) "should indicate that the resource doesn't exist")
;;         (is (= "application/json" (headers "Content-Type")) "should be a json response")
;;         (is (= nil body-json) "body should be empty")))

;;     (testing "GET on /indicators/:id with non numeric id"
;;       (let [service (service-fn sut)
;;             {:keys [status body headers] :as response}
;;             (response-for service :get (url-for :indicators-item-view :path-params {:id "alphaid"}))]

;;         (is (= 404 status) "should be a not found response")
;;         (is (= "text/plain" (headers "Content-Type")) "should be a json response")
;;         (is (= "Not Found" body) "body should be empty when not found")))))