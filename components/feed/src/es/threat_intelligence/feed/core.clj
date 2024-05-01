(ns es.threat-intelligence.feed.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

;; For now we are based on seed data to get the project moving.
(def source
  (json/parse-stream (io/reader (io/resource "feed/indicators.json"))))

(defn feed-items
  "Fetch all indicators from the seed data"
  []
  (get-in (first source) ["indicators"]))

(comment
  (take 1 source)
  (feed-items)
  :rcf)
