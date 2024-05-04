(ns es.threat-intelligence.rest-api.routes.indicators-test
  (:require [es.threat-intelligence.rest-api.routes.indicators :as sut]
            [clojure.test :as t :refer :all]))

(def minimal-indicator-data [{"id" 12345 "type" "IPv4"} {"id" 12346 "type" "FileHash-SHA256"} {"id" 41234 "type" "IPv4"}])

(deftest indicators-test
  (testing "retrieve all indicators"
    (let [context {:indicators minimal-indicator-data :request {}}
          post-context ((:enter sut/get-indicators-interceptor) context)
          {:keys [response]} post-context]
      (is (= 200 (:status response))
          (= minimal-indicator-data (:body response))))))

