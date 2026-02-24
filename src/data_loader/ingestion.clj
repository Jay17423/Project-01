(ns data-loader.ingestion
  (:require
   [cheshire.core :as json]
   [clojure.string :as str]
   [data-loader.db-config :refer [db-connection]])
  (:import
   (org.opensearch.client Request)))

(defn build-request-body
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
    (let [req (Request. "POST" "/_bulk")]
      (.setJsonEntity req (build-request-body index-name batch))
      (.performRequest db-connection req))))

