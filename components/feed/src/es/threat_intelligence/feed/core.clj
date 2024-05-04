(ns es.threat-intelligence.feed.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

;; For now we are based on seed data to get the project moving.
(def source
  (-> (io/resource "feed/indicators.json")
      (io/reader)
      (json/parse-stream)))

(defn feed-items
  "Fetch all indicators from the seed data"
  []
  source)

(comment
  (take 1 (feed-items))
  (feed-items)
  (count (source))
  :rcf)
