(ns es.threat-intelligence.compromise-indicator.interface-test
  (:require [clojure.test :as test :refer :all]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]))

(def empty-indicator-data [])
(def minimal-indicator-data [{"id" 12345} {"id" 12346} {"id" 41234}])

(deftest test-find-by-id-bad-inputs
  (testing "find-by-id with bad inputs"
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data nil)) "with nil id")
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data "1")) "without any data")))

(deftest test-find-by-id
  (testing "find-by-id with successful lookups"
    (is (= nil (compromise-indicator/find-by-id minimal-indicator-data 1)) "should not find with nonexistent id")
    (is (= {"id" 12345} (compromise-indicator/find-by-id minimal-indicator-data 12345)) "should find with valid id")))
