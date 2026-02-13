(ns project-01.core
  (:require
   [project-01.cleaning-logic :as clean]
   [project-01.bulk-post :as bulk]))

(defn -main []
  "Runs the CSV cleaning pipeline and performs bulk ingestion into OpenSearch."
  (try
    (println "Starting CSV cleaning...")
    (clean/write-csv (clean/clean-csv))

    (println "CSV cleaned. Starting bulk ingest...")
    (bulk/bulk-insert!)

    (println "Bulk ingest completed.")

    (catch java.net.ConnectException e
      (println "ERROR: OpenSearch is not running")
      (println (.getMessage e))
      (System/exit 1))

    (catch Exception e
      (println "ERROR:" (.getMessage e))
      (System/exit 1))))
