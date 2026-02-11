(ns project-01.core
  (:require [project-01.bulk-post :as bulk]))

(defn -main []
  (bulk/bulk-insert!))
