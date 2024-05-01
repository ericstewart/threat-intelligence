(ns es.threat-intelligence.feed.interface
  (:require [es.threat-intelligence.feed.core :as core]))

(defn indicators
  "Return all current items in the feed"
  []
  (core/feed-items))
