(ns cleaning-logic
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn read-transactions []
  "This function will read the data from the csv file"
  (with-open [r (io/reader (io/resource "raw/transaction-1k.csv"))]
    (doall (csv/read-csv r))))

(defn clean-row [row]
  "This function will clean the data
   Rules
   1.Convt all amount data into float 
   2. Trim leading and trailing space from all the field data
   3. Convt all the data into lower-case"
  (let [trimmed (map #(str/lower-case (str/trim %)) row)
        amount  (Double/parseDouble (last trimmed))]
    (conj (vec (butlast trimmed)) amount)))

(defn clean-csv []
  "This function will call clean-row and read-transaction function and perfrom
   data cleaning "
  (let [[header & rows] (read-transactions)]
    (cons header (map clean-row rows))))


(defn write-csv [rows]
  "This function will write clean data into the 
   data/cleaned/transactions_clean.csv " 
  (io/make-parents "data/cleaned/transactions_clean.csv")
  (with-open [w (io/writer "data/cleaned/transactions_clean.csv")]
    (csv/write-csv w rows)))

(write-csv (clean-csv))