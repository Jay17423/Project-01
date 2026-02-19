(defproject data-loader "0.1.0-SNAPSHOT"
  :description "CSV cleaning and bulk ingest into OpenSearch"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "1.1.1"] 
                 [cheshire "5.12.0"]
                 [date-clj "1.0.1"]
                 [com.taoensso/timbre "6.5.0"]
                 [com.grammarly/omniconf "0.5.2"] 
                 [org.opensearch.client/opensearch-java "2.11.1"]
                 [org.apache.httpcomponents.client5/httpclient5 "5.2.1"]
                 [com.fasterxml.jackson.core/jackson-databind "2.15.2"]]
  :main data-loader.core
  :repl-options {:init-ns data-loader.core})
