(ns data-loader.clean-data-test
  "Unit tests for CSV cleaning and normalization logic."
  (:require
   [clojure.test :refer [deftest is testing]]
   [data-loader.clean-data :as clean]))

(deftest normalize-string-test
  (testing "Trims and lowercases string values"
    (is (= "bangalore"
           (clean/normalize-string "  Bangalore  "))))

  (testing "Converts non-string values to normalized string"
    (is (= "123"
           (clean/normalize-string 123)))))

(deftest rename-field-test
  (testing "Renames loc to location"
    (is (= "location"
           (clean/rename-field "loc"))))

  (testing "Renames ts to timestamp"
    (is (= "timestamp"
           (clean/rename-field "ts"))))

  (testing "Leaves other fields unchanged"
    (is (= "user"
           (clean/rename-field "user")))))

(deftest parse-double-safe-test
  (testing "Parses decimal amount"
    (is (= 130.5
           (clean/parse-double-safe "130.5"))))

  (testing "Parses integer amount"
    (is (= 416.0
           (clean/parse-double-safe "416"))))

  (testing "Returns 0.0 for invalid amount"
    (is (= 0.0
           (clean/parse-double-safe "abc")))))

(deftest split-item-test
  (testing "Splits item into brand and product"
    (is (= {:brand "marico"
            :product "soap"}
           (clean/split-item "marico_soap"))))

  (testing "Handles another item format"
    (is (= {:brand "dabur"
            :product "salt"}
           (clean/split-item "dabur_salt")))))

(deftest clean-value-test
  (testing "Cleans amount field to double"
    (is (= 130.5
           (clean/clean-value :amount "130.5"))))

  (testing "Splits item field into brand and product"
    (is (= {:brand "marico"
            :product "soap"}
           (clean/clean-value :item "marico_soap"))))

  (testing "Normalizes generic string fields"
    (is (= "bangalore"
           (clean/clean-value :loc " Bangalore ")))))

(deftest row-to-map-test
  (testing "Converts CSV row into cleaned transaction map"

    (let [header ["id" "ts" "user" "item" "market" "loc" "amount"]

          row ["u0009_marico_soap_dailyneeds_bangalore_2019-08-11"
               "2019-08-11"
               "u0009"
               "marico_soap"
               "dailyneeds"
               "bangalore"
               "130.5"]

          result (clean/row->map header row)]

      (is (= "2019-08-11" (:timestamp result)))
      (is (= "u0009" (:user result)))
      (is (= "dailyneeds" (:market result)))
      (is (= "bangalore" (:location result)))
      (is (= 130.5 (:amount result)))
      (is (= "marico" (:brand result)))
      (is (= "soap" (:product result))))))