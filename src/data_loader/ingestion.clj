(ns data-loader.ingestion
  (:require
   [cheshire.core :as json]
   [clj-http.client :as http]
   [clojure.string :as str]))

(defn build-request-body
  "Creates bulk body for ingestion of data into OpenSearch"
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
  "Ingest data into database"
  [db-url index-name docs]
  (http/post
   (str db-url "/_bulk")
   {:headers {"Content-Type" "application/x-ndjson"}
    :body (build-request-body index-name docs)}))

