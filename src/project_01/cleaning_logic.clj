(ns project-01.cleaning-logic
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:import
   [java.time LocalDate]
   [java.time.format DateTimeFormatter DateTimeParseException]))



(defn read-transactions []
  "Read raw CSV data"
  (with-open [r (io/reader (io/resource "raw/transaction-1k.csv"))]
    (doall (csv/read-csv r))))


(def formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd"))

(defn valid-date? [s]
  "Validate the date"
  (try
    (= s (.format (LocalDate/parse s formatter) formatter))
    (catch DateTimeParseException _
      false)))


(defn safe-parse-double [s]
  "Convt amount(int) to float for Atomicity"
  (try
    (Double/parseDouble s)
    (catch Exception _
      0.0)))


(defn clean-row [row]
  "Clean the CSV data by applying above three function
   -> Change all string into lower-case
   -> valid-data? will validate the date
   -> safe-parse-double will change the data type of amount into double"
  (as-> row r
    (map #(str/lower-case (str/trim %)) r)
    (vec r)

    (assoc r 1
           (when (valid-date? (nth r 1))
             (nth r 1)))
    
    (conj (vec (butlast r))
          (safe-parse-double (last r)))))

(defn clean-csv []
  "Apply cleaning logic to all rows"
  (let [[header & rows] (read-transactions)]
    (cons header (map clean-row rows))))

(defn write-csv [rows]
  "Write cleaned data to data/cleaned/transactions_clean.csv"
  (io/make-parents "data/cleaned/transactions_clean.csv")
  (with-open [w (io/writer "data/cleaned/transactions_clean.csv")]
    (csv/write-csv w rows)))

(write-csv (clean-csv))