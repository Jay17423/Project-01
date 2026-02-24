(defproject data-loader "0.1.0-SNAPSHOT"
  :description "CSV cleaning and bulk ingest into OpenSearch"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.opensearch.client/opensearch-rest-client "2.9.0"]
                 [mount "0.1.23"]
                 [org.clojure/data.csv "1.1.1"] 
                 [cheshire "5.12.0"] 
                 [com.taoensso/timbre "6.5.0"]
                 [com.grammarly/omniconf "0.5.2"]]
  :main data-loader.core
  :repl-options {:init-ns data-loader.core})
