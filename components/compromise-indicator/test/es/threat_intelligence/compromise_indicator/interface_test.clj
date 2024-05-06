(ns es.threat-intelligence.compromise-indicator.interface-test
  (:require [clojure.test :as test :refer :all]
            [es.threat-intelligence.compromise-indicator.interface :as compromise-indicator]))

(def empty-indicator-data [])
(def minimal-indicator-data {"abd8828" {"id" "abd8828" "tlp" "green" "author_name" "janesmith" "indicators" [{"id" 12345 "type" "Type1"}]}

                             "84ab8hy" {"id" "84ab8hy" "tlp" "white" "author_name" "alneumann" "indicators" [{"id" 12346 "type" "Type-2"}]}
                             "abc123z" {"id" "abc123z" "tlp" "red" "author_name" "researcher" "indicators" [{"id" 41234 "type" "Type1"}]}})

(defn minimal-indicator-db [] (vals minimal-indicator-data))

(deftest test-filter-nonmatching-indicators
  (testing "filter-nonmatching-indicators"
    (is (= [{"id" "1" "indicators" [{"type" "Type1"}]}] (compromise-indicator/filter-nonmatching-indicators "Type1" [{"id" "1" "indicators" [{"type" "Type1"} {"type" "Type2"}]}])))))

(deftest test-find-by-id-bad-inputs
  (testing "find-by-id with bad inputs"
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data nil)) "with nil id")
    (is (= nil (compromise-indicator/find-by-id empty-indicator-data "1")) "without any data")))

(deftest test-find-by-id
  (testing "find-by-id with successful lookups"
    (is (= nil (compromise-indicator/find-by-id (minimal-indicator-db) 1)) "should not find with nonexistent id")
    (is (= (minimal-indicator-data "abd8828") (compromise-indicator/find-by-id (minimal-indicator-db) "abd8828")) "should find with valid id")))

(deftest test-get-all
  (testing "get all with no filtering"
    (is (= empty-indicator-data (compromise-indicator/get-all empty-indicator-data))) ;
    (is (= (minimal-indicator-db) (compromise-indicator/get-all (minimal-indicator-db))))))

(deftest test-get-all-with-type-filtering
  (testing "get all with type filtering"
    (is (= empty-indicator-data (compromise-indicator/get-all empty-indicator-data "Type2")))
    (is (= [(minimal-indicator-data "abd8828") (minimal-indicator-data "abc123z")]
           (compromise-indicator/get-all (minimal-indicator-db) "Type1")))
    (is (= [(minimal-indicator-data "84ab8hy")] (compromise-indicator/get-all (minimal-indicator-db) "Type-2")))
    (is (= [] (compromise-indicator/get-all (minimal-indicator-db) "not-an-actual-type")))))

(deftest test-indicator-search
  (testing "search with nil params"
    (is (= [] (compromise-indicator/search (minimal-indicator-db) nil))))

  (testing "search with no params"
    (is (= [] (compromise-indicator/search (minimal-indicator-db) {}))))

  (testing "search with bad params"
    (is (= [] (compromise-indicator/search (minimal-indicator-db) "foo"))))

  (testing "search with invalid params"
    (is (= [] (compromise-indicator/search (minimal-indicator-db) {:does-not-exist "in source data"}))))

  (testing "search with a single search field"
    (is (= [(minimal-indicator-data "abd8828")]
           (compromise-indicator/search (minimal-indicator-db) {"tlp" "green"}))))

  (testing "search with multiple search fields"
    (is (= [(minimal-indicator-data "84ab8hy") (minimal-indicator-data "abc123z")]
           (compromise-indicator/search (minimal-indicator-db) {"author_name" "researcher" "tlp" "white"}))
        "should match against any field values")))
