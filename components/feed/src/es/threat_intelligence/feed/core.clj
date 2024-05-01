(ns es.threat-intelligence.feed.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(def source
  (json/parse-stream (io/reader (io/resource "feed/indicators.json"))))

(defn feed-items
  []
  (get-in source ["indicators"]))

(comment
  (take 1 source)
  (feed-items)
  :rcf)
