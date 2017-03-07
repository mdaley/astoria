(ns astoria.core-test
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all])
  (:import [com.google.cloud.datastore.testing LocalDatastoreHelper]
           [org.joda.time Duration]))

(deftest store-something
  (testing "I'm going to store something"
    (let [ds (get-datastore {:host "http://localhost:8123"
                             :project-id "project"})
          kind (create-kind ds "kind")
          entity-key (create-key kind "key")
          entity (create-entity entity-key "text")]
      (store-entity ds entity)
      (let [retrieved-entity (retrieve-entity ds entity-key)]
        (is (= (.getString retrieved-entity "text") "text"))))))
