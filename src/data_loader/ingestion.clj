(ns data-loader.ingestion
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
  [index-name docs]
  (doseq [batch (partition-all 1000 docs)]
    (try
      (.performRequest
       db-connection
       (doto (Request. "POST" "/_bulk")
         (.setJsonEntity (build-req-body index-name batch))))
      (catch Exception e
        (log/error e "Bulk ingestion failed")
        (throw e)))))

