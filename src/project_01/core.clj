(ns project-01.core
  (:require
   [project-01.cleaning-logic :as clean]
   [project-01.bulk-post :as bulk]))

(defn -main []
  (println "Starting CSV cleaning...")
  (clean/write-csv (clean/clean-csv))

  (println "CSV cleaned. Starting bulk ingest...")
  (bulk/bulk-insert!)

  (println "Bulk ingest completed."))
