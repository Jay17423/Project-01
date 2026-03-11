(ns data-loader.clean-data
  "Provides functions to clean and normalize and read raw data before
   ingestion."
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [taoensso.timbre :as log]))

(defn normalize-string
  "Trim and convert input to lower-case string"
  [value]
  (-> value
      str
      str/trim
      str/lower-case))

(defn rename-field
  "Change loc to location and ts to timestamp"
  [field]
  (case field
    "loc" "location"
    "ts"  "timestamp"
    field))

(defn parse-double-safe
  "Parses a value as double. Returns 0.0 if it cannot be parsed."
  [value]
  (try
    (double (Double/parseDouble (str value)))
    (catch Exception _ 0.0)))

(defn split-item
  "Split item into brand and product"
  [item]
  (let [[brand prod] (str/split (normalize-string item) #"_")]
    {:brand brand
     :product prod}))

(defn clean-value
  "Apply cleaning logic on the basic of key"
  [k v]
  (cond
    (= k :amount) (parse-double-safe v)
    (= k :item) (split-item v)
    (string? v) (normalize-string v)
    :else v))

(defn row->map
  "Convt row data into map with header as key and row as value"
  [header row]
  (->> (zipmap header row)
       (map
        (fn [[k v]]
          (let [field (keyword (rename-field (normalize-string k)))]
            (if (= field :item)
              (merge
               {k (normalize-string v)}
               (split-item v))
              {field (clean-value field v)}))))
       (apply merge)))

(defn read-and-clean-csv
  "Read CSV and return seq of cleaned maps"
  [resource-path]
  (log/info "Starting CSV read and clean process" {:path resource-path})
  (let [res (io/resource resource-path)]
    (when-not res
      (log/error {:msg "CSV file not found"
                  :path resource-path})
      (throw (ex-info "CSV file not found" {:path resource-path})))
    (with-open [read (io/reader res)]
      (let [data (->> (csv/read-csv read)
                      ((fn [[header & rows]]
                         (mapv #(row->map header %) rows))))]
        (log/info "CSV read and cleaned successfully"
                  {:path resource-path
                   :records (count data)})
        data))))