(ns data-loader.config
  "Handles loading and accessing application configuration."
  (:require
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]))

(defn load-config!
  "Load and verify application configuration"
  [path]
  (try
    (cfg/populate-from-file path)
    (cfg/verify)
    (catch Exception err
      (log/error  {:msg "Configuration loading failed"
                   :error (.getMessage err)})
      (throw err))))