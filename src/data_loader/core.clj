(ns data-loader.core
  (:require
   [mount.core :as mount]
   [data-loader.clean-data :as clean]
   [data-loader.ingestion :as ingest]
   [data-loader.config]
   [data-loader.db-config]
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]))

(defn -main []
  (try
    (cfg/populate-from-file "config.edn")
    (cfg/verify :silent true)
    (mount/start)
    (let [docs (clean/read-and-clean-csv (cfg/get :input-path))]
      (ingest/ingest-data!
       (cfg/get :index-name)
       docs))
    (mount/stop)
    (catch Exception e
      (log/error e "Pipeline failed")
      (mount/stop)
      (System/exit 1))))