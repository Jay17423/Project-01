(ns data-loader.core
  "Entry point for the data loader service."
  (:require
   [mount.core :as mount]
   [data-loader.clean-data :as clean]
   [data-loader.ingestion :as ingest]
   [data-loader.db-config]
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]
   [data-loader.config :as cnfg]))

(defn -main
  "Runs the data ingestion pipeline."
  []
  (try
    (log/info "Starting data ingestion pipeline")
    (cnfg/load-config! "config/config.edn")
    (mount/start)
    (let [docs (clean/read-and-clean-csv (cfg/get :input :path))]
      (log/info "Read CSV records" {:count (count docs)})
      (ingest/ingest-data!
       (cfg/get :db :index-name)
       docs))
    (mount/stop)
    (log/info "Pipeline completed successfully")
    (catch Exception err
      (log/error err "Data ingest pipeline failed")
      (mount/stop)
      (System/exit 1))))