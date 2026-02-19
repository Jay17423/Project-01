(ns data-loader.core
  (:require
   [data-loader.clean-data :as clean]
   [data-loader.ingestion :as ingest]
   [data-loader.config]        
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]))

(defn -main
  []
  (try 
    (cfg/populate-from-file "config.edn")
    (cfg/verify :silent true) 
    (let [docs (clean/read-and-clean-csv (cfg/get :input-path))]
      (ingest/ingest-data!
       (cfg/get :db-url)
       (cfg/get :index-name)
       docs)) 
    (catch Exception e
      (log/error e "Pipeline failed")
      (System/exit 1))))
