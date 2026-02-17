(ns project-01.core
  (:require
   [project-01.cleaning-logic :as clean]
   [project-01.bulk-post :as bulk]))

(defn -main []
  "Runs the CSV cleaning pipeline and performs bulk ingestion into OpenSearch."
  (try 
    (clean/write-csv (clean/clean-csv))
    (bulk/bulk-insert!) 
    (catch java.net.ConnectException e
      (println "ERROR: OpenSearch is not running")
      (println (.getMessage e))
      (System/exit 1))

    (catch Exception e
      (println "ERROR:" (.getMessage e))
      (System/exit 1))))
