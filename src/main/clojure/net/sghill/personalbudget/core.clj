(ns net.sghill.personalbudget.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clj-time.format :as f]
            [clojure.math.numeric-tower :as math]
            [clojure.core.reducers :as r]))

(def ynab-format (f/formatter "MM/dd/yyyy"))
(def amex-format (f/formatter "MM/dd/yyyy  E"))

(defn to-ynab-date [date]
  (f/unparse-local-date ynab-format date))

(defn to-ynab-vector [m]
  (vector (to-ynab-date (get m :date)) (get m :payee) (get m :category) (get m :memo) (get m :outflow) (get m :inflow)))

(defn to-ynab [budget-entries]
  (cons ["Date" "Payee" "Category" "Memo" "Outflow" "Inflow"]
        (into [] (r/map to-ynab-vector budget-entries))))

(defn write-ynab-csv [filepath budget-entries]
  (with-open [out-file (io/writer filepath)]
    (csv/write-csv out-file (to-ynab budget-entries))))

(defn from-amex-vector [v]
  (let [amount (bigdec (nth v 7))]
    {:date (f/parse-local-date amex-format (first v)) :payee (nth v 2) (if (neg? amount) :inflow :outflow) (math/abs amount)}))

(defn read-amex-file [filepath]
  (with-open [out-file (io/reader filepath)]
    (doall
      (csv/read-csv out-file))))

(defn amex-to-ynab [amex-filepath ynab-filepath]
  (write-ynab-csv ynab-filepath
                  (map from-amex-vector
                       (read-amex-file amex-filepath)))
  )
