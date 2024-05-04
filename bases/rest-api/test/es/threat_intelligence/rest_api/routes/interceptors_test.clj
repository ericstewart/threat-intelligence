(ns es.threat-intelligence.rest-api.routes.interceptors-test
  (:require [es.threat-intelligence.rest-api.routes.interceptors :as sut]
            [clojure.test :refer :all]))


(deftest test-transform-content
  (testing "nil content type"
    (is (= nil (sut/transform-content nil nil)))
    (is (= "" (sut/transform-content "" nil)) "empty string body")
    (is (= "string body" (sut/transform-content "string body" nil)) "string body")
    (is (= {:foo :bar} (sut/transform-content {:foo :bar} nil)) "map body")
    (is (= [{}] (sut/transform-content [{}] nil)) "array body"))

  (testing "json content type"
    (is (= "null" (sut/transform-content nil "application/json")))
    (is (= "\"\"" (sut/transform-content "" "application/json")) "empty string body")
    (is (= "\"string body\"" (sut/transform-content "string body" "application/json")) "string body")
    (is (= "{\"foo\":\"bar\"}" (sut/transform-content {:foo :bar} "application/json")) "map body")
    (is (= "[{}]" (sut/transform-content [{}] "application/json")) "array body")))

(deftest test-coerce-body-interceptor
  (testing "coercion when no existing Content Type"
    (let [context {:request {} :response {:body nil}}
          leave-fn (sut/coerce-body-interceptor :leave)
          updated-context (leave-fn context)]
      (is (= nil (get-in updated-context [:response :body])))))

  (testing "coercion with existing Content Type"
      (let [context {:request {} :response {:headers {"Content-Type" "application/json"} :body nil}}
            leave-fn (sut/coerce-body-interceptor :leave)
            updated-context (leave-fn context)]
        (is (= nil (get-in updated-context [:response :body]))))))

(deftest test-coerce-to-fn
  (testing "coercion with no content type"
    (is (= {:body nil :headers {"Content-Type" nil}} (sut/coerce-to {:body nil} nil))) "Coercion of a nil body"
    (is (= {:body {} :headers {"Content-Type" nil}} (sut/coerce-to {:body {}} nil))) "Coercion of an empty body"
    (is (= {:body [] :headers {"Content-Type" nil}} (sut/coerce-to {:body []} nil))) "Coercion of an array body")

  (testing "coercion with json content type"
    (is (= {:body nil :headers {"Content-Type" nil}} (sut/coerce-to {:body nil} "application/json"))) "Coercion of a nil body"
    (is (= {:body "{}" :headers {"Content-Type" "application/json"}} (sut/coerce-to {:body {}} "application/json"))) "Coercion of an empty body"
    (is (= {:body "[]" :headers {"Content-Type" "application/json"}} (sut/coerce-to {:body []} "application/json"))) "Coercion of an array body"))
