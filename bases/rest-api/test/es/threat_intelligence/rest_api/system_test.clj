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

(def minimal-indicator-data [{"id" 12345 "type" "IPv4"} {"id" 12346 "type" "FileHash-SHA256"} {"id" 41234 "type" "IPv4"}])

(def minimal-indicator-data {"abd8828" {"id" "abd8828" "tlp" "green" "author_name" "janesmith" "indicators" [{"id" 12345 "type" "IPv4"}]}
                             "84ab8hy" {"id" "84ab8hy" "tlp" "white" "author_name" "alneumann" "indicators" [{"id" 12346 "type" "FileHash-SHA256"}]}
                             "abc123z" {"id" "abc123z" "tlp" "red" "author_name" "researcher" "indicators" [{"id" 41234 "type" "IPv4"}]}})

(defn minimal-indicator-db [] (vals minimal-indicator-data))
;;---------------------------------------------------------------
;; Technique borrowed from clojure-polylith-relworld-example-app
;; as a way to stub data for the system under test
(defn stub-components
  [f]
  (with-redefs [feed/indicators (fn [] (minimal-indicator-db))]
    (f)))

(use-fixtures :each stub-components)
;;----------------------------------------------------------------

;;----------------------------------------------------------------
;; Borrowed from Pedestal docs on testing
(def url-for (route/url-for-routes routes/routes))

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
            body-json (json/read-str body)]
        (is (= 200 status) "should be a successful response")
        (is (= "application/json" (headers "Content-Type")) "should be a json response")
        (is (= (minimal-indicator-db) body-json) "body should contain expected records")))

    (testing "GET on /indicators with Accept header set to an invalid type"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators) :headers {"Accept" "text/plain"})]

        (is (= 406 status) "should be a not acceptable response")
        (is (= "application/json" (headers "Content-Type")) "should be JSON")
        (is (= "\"Not Acceptable\"" body) "body should indicate 'Not Acceptable'")))

    (testing "GET on /indicators with type filtering"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators :query-params {:type "IPv4"}))
            body-json (json/read-str body)]

        (is (= 200 status) "should be a successful response")
        (is (= "application/json" (headers "Content-Type")) "should be a json response")
        (is (= 2 (count body-json)) "body should contain expected records")
        (is (= [(minimal-indicator-data "abd8828") (minimal-indicator-data "abc123z")] body-json) "body should contain expected records")))

    (testing "GET on /indicators with type filtering (type with escaped characters)"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators :query-params {:type "FileHash-SHA256"}))
            body-json (json/read-str body)]

        (is (= 200 status) "should be a successful response")
        (is (= "application/json" (headers "Content-Type")) "should be a json response")
        (is (= 1 (count body-json)) "body should contain expected records")
        (is (= [(minimal-indicator-data "84ab8hy")] body-json) "body should contain expected records")))))

(deftest get-single-indicator-test
  (with-system [sut (sut/new-system :test)]

    (testing "GET on /indicators/:id"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators-item-view :path-params {:id "84ab8hy"}))
            body-json (json/read-str body)]

        (is (= 200 status) "should be a successful response")
        (is (= "application/json;charset=UTF-8" (headers "Content-Type")) "should be a json response")
        (is (= (minimal-indicator-data "84ab8hy") body-json) "body should contain the specific record")))

    (testing "GET on /indicators/:id with id that doesn't exist"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators-item-view :path-params {:id 99999}))]
            ;; body-json (json/read-str body)]

        (is (= 404 status) "should indicate that the resource doesn't exist")
        (is (= nil (headers "Content-Type")) "should have no content type")
        (is (= "" body) "body should be empty when not found")))

    (testing "GET on /indicators/:id with non numeric id"
      (let [service (service-fn sut)
            {:keys [status body headers] :as response}
            (response-for service :get (url-for :indicators-item-view :path-params {:id "alphaid"}))]

        (is (= 404 status) "should be a not found response")
        (is (= nil (headers "Content-Type")) "should have no content type")
        (is (= "" body) "body should be empty when not found")))))

(deftest test-indicator-search
  (with-system [sut (sut/new-system :test)]
    (testing "search with no params"
      (let [service (service-fn sut)
            url (url-for :indicators-search)
            {:keys [status body headers] :as response}
            (response-for service :post url :body "{}")
            body-json (json/read-str body)]

        (is (= "/indicators/search" url))
        (is (= 200 status) "should be a not found response")
        (is (= "application/json;charset=UTF-8" (headers "Content-Type")) "should be a json response")
        (is (= [] body-json))))

    ;; Ideally, a malformed JSON payload would result in a 400 status code on teh response, but I'll
    ;; need some more time with Pedestal to figure out the best way to do that.  Also, I am still
    ;; getting a 200 via the following test, but trying this with curl results in a 500.
    #_(testing "search with invalid payload"
        (let [service (service-fn sut)
              url (url-for :indicators-search :headers {"Content-Type" "application/json"})
              {:keys [status body headers] :as response}
              (response-for service :post url :body "{\"foo: 1234")]

          (is (= "/indicators/search" url))
          (is (= 400 status) "should be a bad request response")
          #_(is (= "application/json;charset=UTF-8" (headers "Content-Type")) "should be a json response")
          (is (= nil body))))

    (testing "search with params"
      (let [service (service-fn sut)
            url (url-for :indicators-search)
            {:keys [status body headers] :as response}
            (response-for service
                          :post url
                          :body (json/write-str {"author_name" "janesmith"})
                          :headers {"Content-Type" "application/json" "Accept" "application/json"})
            body-json (json/read-str body)]

        (is (= "/indicators/search" url))
        (is (= 200 status) "should be a not found response")
        (is (= "application/json;charset=UTF-8" (headers "Content-Type")) "should be a json response")
        (is (= [(minimal-indicator-data "abd8828")] body-json))))))
