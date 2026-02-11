(ns project-01.bulk-ingest
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn split-item [item]
  (let [[brand product] (str/split item #"_")]
    {:brand brand
     :product product}))

(defn read-csv []
  (with-open [r (io/reader "data/cleaned/transactions_clean.csv")]
    (let [[_ & rows] (csv/read-csv r)]
      (doall
       (map (fn [[id ts user item market loc amount]]
              (merge
               {:id id
                :ts ts
                :user user
                :market market
                :loc loc
                :amount (Double/parseDouble amount)}
               (split-item item)))
            rows)))))
