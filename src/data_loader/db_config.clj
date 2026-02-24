(ns data-loader.db-config
  (:require
   [mount.core :refer [defstate]]
   [omniconf.core :as cfg])
  (:import
   (org.opensearch.client RestClient)
   (org.apache.http HttpHost)))

(defstate db-connection
  :start
  (.build
   (RestClient/builder
    (into-array HttpHost
                [(HttpHost/create (cfg/get :db-url))])))
  :stop
  (.close  db-connection))