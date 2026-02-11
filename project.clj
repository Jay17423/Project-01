(defproject project-01 "0.1.0-SNAPSHOT"
  :description "CSV cleaning and bulk ingest into OpenSearch"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "1.1.1"]
                 [clj-http "3.12.3"]
                 [cheshire "5.12.0"]
                 [date-clj "1.0.1"]]
  :main project-01.core
  :repl-options {:init-ns project-01.core})
