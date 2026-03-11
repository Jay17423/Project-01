(ns data-loader.ingestion
  "Handles batching and ingestion of data into the database."
  (:require
   [cheshire.core :as json]
   [clojure.string :as str]
   [data-loader.db-config :refer [db-connection]]
   [taoensso.timbre :as log])
  (:import
   (org.opensearch.client Request)))

(defn build-req-body
  "Creates bulk body for ingestion of data into database"
  [index-name docs]
  (->> docs
       (mapcat (fn [doc]
                 [{:create {:_index index-name
                            :_id (:id doc)}}
                  doc]))
       (map json/generate-string)
       (str/join "\n")
       (#(str % "\n"))))

(defn ingest-data!
  "Ingest data into the database in the batch of 1000 data on each request."
  [index-name docs]
  (log/info "Starting bulk ingestion" {:index index-name
                                       :total-docs (count docs)})
  (doseq [batch (partition-all 1000 docs)]
    (try
      (log/info "Sending bulk batch" {:batch-size (count batch)})
      (.performRequest
       db-connection
       (doto (Request. "POST" "/_bulk")
         (.setJsonEntity (build-req-body index-name batch))))
      (catch Exception err
        (log/error {:msg "Bulk ingestion failed"
                    :index index-name
                    :error (.getMessage err)})
        (throw err)))))