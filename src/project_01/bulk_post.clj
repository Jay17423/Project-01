(ns project-01.bulk-post
  (:require
   [cheshire.core :as json]
   [clj-http.client :as http]
   [clojure.string :as str]
   [project-01.bulk-ingest :refer [read-csv]]))

(def opensearch-url "http://localhost:9200")
(def index-name "transactions")

(defn bulk-body [docs]
  "Creates bulk body for ingestion of data into OpenSearch"
  (->> docs
       (mapcat (fn [doc]
                 [{:create {:_index index-name
                            :_id (:id doc)}}
                  doc]))
       (map json/generate-string)
       (str/join "\n")
       (#(str % "\n"))))

(defn bulk-insert! []
  "Ingest the data into OpenSearch using BulkApi"
  (let [docs (read-csv)]
    (http/post
     (str opensearch-url "/_bulk")
     {:headers {"Content-Type" "application/x-ndjson"}
      :body (bulk-body docs)})))


