(ns es.threat-intelligence.feed.interface-test
  (:require [clojure.test :as test :refer :all]
            [es.threat-intelligence.feed.interface :as feed]))

;; Since the current implementation is based off a set of fixed seed data, verify
;; That we hav eloaded that
(deftest test-indicators-load
  (is (= 588 (count (feed/indicators)))))
