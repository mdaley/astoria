(ns astoria.acceptance.acceptance
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all]))

(deftest ^:acceptance store-something
  (let [ds (get-datastore {:host "http://localhost:8123"
                           :project-id "some-project"
                           :namespace "some-namespace"})
        kind (create-kind ds "kind")
        entity-key (create-key kind "key")
        entity (create-entity entity-key "text")]
    (store-entity ds entity)
    (let [retrieved-entity (retrieve-entity ds entity-key)]
      (is (= (.getString retrieved-entity "text") "text")))))

(deftest ^:acceptance multiple-namespaces-can-be-used
  (let [ds1 (get-datastore {:host "http://localhost:8123"
                            :project-id "some-project"
                            :namespace "namespace1"})
        ds2 (get-datastore {:host "http://localhost:8123"
                            :project-id "some-project"
                            :namespace "namespace2"})
        kind1 (create-kind ds1 "kind")
        kind2 (create-kind ds2 "kind")
        entity-key1 (create-key kind1 "key")
        entity-key2 (create-key kind2 "key")
        entity1 (create-entity entity-key1 "text")
        entity2 (create-entity entity-key2 "different text")]
    (store-entity ds1 entity1)
    (store-entity ds2 entity2)
    (let [retrieved-entity1 (retrieve-entity ds1 entity-key1)
          retrieved-entity2 (retrieve-entity ds2 entity-key2)]
      (is (= (.getString retrieved-entity1 "text") "text"))
      (is (= (.getString retrieved-entity2 "text") "different text")))))
