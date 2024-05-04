(ns es.threat-intelligence.compromise-indicator.interface-test
  (:require [clojure.test :as test :refer :all]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]))

(def empty-indicator-data [])
(def minimal-indicator-data [{"id" 12345 "type" "Type1"} {"id" 12346 "type" "Type-2"} {"id" 41234 "type" "Type1" "author_name" "carefulresearcher"}])

(deftest test-find-by-id-bad-inputs
  (testing "find-by-id with bad inputs"
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data nil)) "with nil id")
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data "1")) "without any data")))

(deftest test-find-by-id
  (testing "find-by-id with successful lookups"
    (is (= nil (compromise-indicator/find-by-id minimal-indicator-data 1)) "should not find with nonexistent id")
    (is (= {"id" 12345 "type" "Type1"} (compromise-indicator/find-by-id minimal-indicator-data 12345)) "should find with valid id")))

(deftest test-get-all
  (testing "get all with no filtering"
    (is (= empty-indicator-data (compromise-indicator/get-all empty-indicator-data)))
    (is (= minimal-indicator-data (compromise-indicator/get-all minimal-indicator-data)))))

(deftest test-get-all-with-type-filtering
  (testing "get all with type filtering"
    (is (= empty-indicator-data (compromise-indicator/get-all empty-indicator-data "Type2")))
    (is (= [{"id" 12345 "type" "Type1"} {"id" 41234 "type" "Type1" "author_name" "carefulresearcher"}] (compromise-indicator/get-all minimal-indicator-data "Type1")))
    (is (= [{"id" 12346 "type" "Type-2"}] (compromise-indicator/get-all minimal-indicator-data "Type-2")))
    (is (= [] (compromise-indicator/get-all minimal-indicator-data "not-an-actual-type")))))

(deftest test-indicator-search
  (testing "search with nil params"
    (is (= [] (compromise-indicator/search minimal-indicator-data nil))))

  (testing "search with no params"
    (is (= [] (compromise-indicator/search minimal-indicator-data {}))))

  #_(testing "search with bad params"
      (is (= [] (compromise-indicator/search minimal-indicator-data "foo"))))

  (testing "search with invalid params"
    (is (= [] (compromise-indicator/search minimal-indicator-data {:does-not-exist "in source data"}))))

  (testing "search with a single search field"
    (is (= [{"id" 12345 "type" "Type1"} {"id" 41234 "type" "Type1" "author_name" "carefulresearcher"}] (compromise-indicator/search minimal-indicator-data {"type" "Type1"}))))

  (testing "search with multiple search fields"
    (is (= [{"id" 12346 "type" "Type-2"} {"id" 41234 "type" "Type1" "author_name" "carefulresearcher"}] (compromise-indicator/search minimal-indicator-data {"type" "Type-2" "author_name" "carefulresearcher"})) "should match against any field values")))
