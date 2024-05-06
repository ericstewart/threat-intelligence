(ns es.threat-intelligence.compromise-indicator.core
  (:require [clojure.data :as data]
            [clojure.walk :as walk]
            [es.threat-intelligence.log.interface :as log]))

(defn filter-nonmatching-indicators
  [type-filter indicator-data]
  (log/info "filter-nonmatching")
  (mapv (fn [ioc]
          (do
            (log/info ioc)
            (update-in ioc ["indicators"]
                       (fn [old-indicators]
                         (filterv (fn [ind] (= type-filter (ind "type")))
                                  old-indicators)))))
        indicator-data))



(defn filter-on-type
  [type-filter indicator-data]
  (->> indicator-data
       (filterv (fn [ioc]
                  (some (fn [ind]
                          (= type-filter (ind "type")))
                        (get-in ioc ["indicators"]))))
       (filter-nonmatching-indicators type-filter)
       #_(mapv (fn [ioc])
               (update-in ioc ["indicators"]
                          #(filterv (fn [ind] (= type-filter (ind "type")))
                                    (get-in % ["indicators"]))))))

(defn find-by-id
  "Given indicator data and the unique identifier of a Compromise Indicatgor,
  returns a Map representing the data for the indicator."
  [indicator-data indicator-id]
  (do
    (log/info "find-by-id called with id:" indicator-id)
    (->> indicator-data
         (filter #(= (str (% "id")) (str indicator-id)))
         first)))

(defn get-all
  "Return all indicators.

   If a type-filter parameter is given, return only indicators
   that match the specified type"
  ([indicator-data]
   (get-all indicator-data nil))
  ([indicator-data type-filter]
   (do
     (log/info "get-all called with type-filter: " type-filter)
     (if type-filter
       (->> indicator-data
            (filter-on-type type-filter))
       indicator-data))))

(defn search
  "Search indicators based on a hash of one or more criteria"
  [indicator-data search-criteria]
  (try
    (let [modified-search (clojure.walk/stringify-keys search-criteria)]
      (do
        (log/info "search called with criteria for :" (str (keys modified-search)))
        (filterv (fn [indicator]
                   (let [[_ _ same] (clojure.data/diff indicator modified-search)]
                     (not (empty? same))))
                 indicator-data)))
    (catch java.lang.ClassCastException e [])))

(comment
  (get-all [{"type" "FileHash-SHA256"}] "FileHash-SHA256")
  (get-all [{"id" "foo" "indicators" [{"type" "FileHash-SHA256"}]}] "FileHash-SHA256"))
