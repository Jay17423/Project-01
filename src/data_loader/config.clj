(ns data-loader.config
  (:require
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]))

(cfg/define
  {:input-path {:description "CSV file path"
                :type :string
                :required true}

   :db-url     {:description "Database URL"
                :type :string
                :required true}

   :index-name {:description "Database index name"
                :type :string
                :required true}})


(defn load-config!
  "Load and verify application configuration"
  [path]
  (try
    (cfg/populate-from-file path)
    (cfg/verify)
    (catch Exception err
      (log/error err "Configuration loading failed")
      (throw (ex-info "Invalid configuration" {:config path} err)))))