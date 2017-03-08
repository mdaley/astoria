(ns astoria.unit.unit
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all]))

(deftest unit-tests-go-here
  (testing "unit tests here"
    (is (= 2 (+ 1 1)))))

(deftest contruct-datastore-with-no-parameters-uses-defaults
  (let [ds (get-datastore)
        opts (.getOptions ds)]
    (is (= "https://datastore.googleapis.com" (.getHost opts)))))
