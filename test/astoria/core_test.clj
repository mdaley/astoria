(ns astoria.core-test
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all])
  (:import [com.google.cloud.datastore
            DatastoreOptions
            Datastore]))

(deftest a-test
  (testing "I pass. Yippee!"
    (is (= 1 1))))

(deftest store-something
  (testing "I'm going to store something"
    ))
