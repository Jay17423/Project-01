(ns data-loader.ingestion-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [data-loader.ingestion :as ingest]
   [omniconf.core :as cfg]
   [taoensso.timbre :as log]))

(deftest build-req-body-test
  (testing "Creates bulk request body for transaction document"

    (let [docs [{:id "u0009_marico_soap_dailyneeds_bangalore_2019-08-11"
                 :timestamp "2019-08-11"
                 :user "u0009"
                 :item "marico_soap"
                 :brand "marico"
                 :product "soap"
                 :market "dailyneeds"
                 :location "bangalore"
                 :amount 130.5}]

          body (ingest/build-req-body "transactions" docs)]
      (is (string? body))
      (is (.contains body "\"create\""))
      (is (.contains body "\"_index\":\"transactions\""))
      (is (.contains body "marico"))
      (is (.contains body "bangalore"))
      (is (.contains body "130.5")))))