(ns data-loader.clean-data
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn normalize-string
  "Trim and convert input to lower-case string"
  [value]
  (-> value
      str
      str/trim
      str/lower-case))

(defn parse-double-safe
  "Parse value as double; return 0.0 if not parsable"
  [value]
  (try
    (double (Double/parseDouble (str value)))
    (catch Exception _
      0.0)))

(defn split-item
  "Split item into brand and product"
  [item]
  (let [[brand prod] (str/split (normalize-string item) #"_")]
    {:brand brand
     :product  prod}))

(defn clean-value
  "Apply cleaning logic on the basic of key"
  [k v]
  (cond
    (= k :amount) (parse-double-safe v)
    (= k :item)   (split-item v)
    (string? v)   (normalize-string v)
    :else         v))

(defn row->map
  "This will convt row data into map with header as key and row as value"
  [header row]
  (->> (zipmap header row)
       (map
        (fn [[k v]]
          (let [key (keyword (normalize-string k))]
            (if (= key :item)
              (split-item v)
              {key (clean-value key v)}))))
       (apply merge)))

(defn read-and-clean-csv
  "Read CSV and return seq of cleaned maps"
  [resource-path]
  (let [res (io/resource resource-path)]
    (when-not res
      (throw (ex-info "CSV file not found" {:path resource-path})))
    (with-open [read (io/reader res)]
      (->> (csv/read-csv read)
           ((fn [[header & rows]]
              (doall (map #(row->map header %) rows))))))))
