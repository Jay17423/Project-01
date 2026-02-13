(ns project-01.bulk-ingest
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn split-item [item]
  "Split the item into brand and product"
  (let [[brand product] (str/split item #"_")]
    {:brand brand
     :product product}))

(defn read-csv []
  "Reads the cleaned transactions CSV file and returns a sequence of maps,
   skipping the header row and parsing the amount field as Double."
  (let [res (io/file "data/cleaned/transactions_clean.csv")]
    (when-not res
      (throw 
       (Exception. "CSV file not found: data/cleaned/transactions_clean.csv")))
    (with-open [r (io/reader res)]
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
              rows))))))

