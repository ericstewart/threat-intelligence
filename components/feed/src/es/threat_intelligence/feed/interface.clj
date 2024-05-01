(ns es.threat-intelligence.feed.interface
  (:require [es.threat-intelligence.feed.core :as core]))

(defn indicators
  "Return all current items in the feed.  This is a raw,
   unsorted list of indicators"
  []
  (core/feed-items))
