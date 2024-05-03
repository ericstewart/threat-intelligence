(ns es.threat-intelligence.feed.core-test
  (:require [es.threat-intelligence.feed.core :as sut]
            [clojure.test :as test :refer :all]))

;; Since the current implementation is based off a set of fixed seed data, verify
;; That we hav eloaded that
(deftest test-indicators-load
  (is (= 18077 (count (sut/feed-items)))))
