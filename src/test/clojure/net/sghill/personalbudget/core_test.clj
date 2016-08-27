(ns net.sghill.personalbudget.core_test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [net.sghill.personalbudget.core :as core]))

; ynab

(deftest to-ynab
  (is (= [["Date" "Payee" "Category" "Memo" "Outflow" "Inflow"]]
         (core/to-ynab (list))))
  (is (= [["Date" "Payee" "Category" "Memo" "Outflow" "Inflow"]
          ["07/25/2010" "Sample Payee" nil "Sample Memo" 100.00M nil]]
         (core/to-ynab (list {:date (t/local-date 2010 7 25) :payee "Sample Payee" :memo "Sample Memo" :outflow 100.00M})))))

(deftest to-ynab-date
  (is (= "07/25/2010" (core/to-ynab-date (t/local-date 2010 7 25)))))

(deftest to-ynab-vector
  (is (= ["07/25/2010" "Sample Payee" nil "Sample Memo" 100.00M nil]
         (core/to-ynab-vector {:date (t/local-date 2010 7 25) :payee "Sample Payee" :memo "Sample Memo" :outflow 100.00M}))))

; amex

(deftest from-amex-vector
  (is (= {:date (t/local-date 2016 6 17) :payee "SAFEWAY" :inflow 50.65M}
         (core/from-amex-vector ["06/17/2016  Fri" nil "SAFEWAY" "C F FROST" "XXXX-XXXXXX-10000" nil nil "-50.65" nil nil nil nil nil nil nil nil])))
  (is (= {:date (t/local-date 2016 6 18) :payee "SAFEWAY" :outflow 100.71M}
         (core/from-amex-vector ["06/18/2016  Sat" nil "SAFEWAY" "C F FROST" "XXXX-XXXXXX-10000" nil nil "100.71" nil nil nil nil nil nil nil nil]))))

(run-tests)
