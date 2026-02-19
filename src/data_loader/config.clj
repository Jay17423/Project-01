(ns data-loader.config
  (:require
   [omniconf.core :as cfg]))

(cfg/define
  {:input-path {:description "CSV file path"
                :type :string
                :required true}

   :db-url     {:description "OpenSearch URL"
                :type :string
                :required true}

   :index-name {:description "OpenSearch index name"
                :type :string
                :required true}})
