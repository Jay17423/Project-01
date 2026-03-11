(ns data-loader.db-config
  "Manages database connection setup and lifecycle."
  (:require
   [mount.core :refer [defstate]]
   [omniconf.core :as cfg]
   [taoensso.timbre :as log])
  (:import
   (org.opensearch.client RestClient)
   (org.apache.http HttpHost)))

(defstate db-connection
  "Manages the lifecycle of the database RestClient connection."
  :start (try
           (let [db-url (cfg/get :db :url)]
             (log/info "Starting OpenSearch connection" {:db-url db-url})
             (.build
              (RestClient/builder
               (into-array HttpHost
                           [(HttpHost/create db-url)]))))
           (catch Exception err
             (log/error {:msg "Failed to create OpenSearch connection"
                         :error (.getMessage err)})
             (throw err)))

  :stop (try
          (log/info "Closing OpenSearch connection")
          (.close db-connection)
          (catch Exception err
            (log/error {:msg "Failed to close OpenSearch connection"
                        :error (.getMessage err)})
            (throw err))))