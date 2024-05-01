(ns es.threat-intelligence.rest-api.routes.utils)

(defn response
  "Create a response map given the specified status
   and body and include any optional headers."
  [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))
