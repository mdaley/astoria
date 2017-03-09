(ns astoria.acceptance.acceptance
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all])
  (:import [java.util UUID]))

(defn- guid
  []
  (str (UUID/randomUUID)))

(deftest ^:acceptance can-store-and-retrieve-basic-entity
  (let [k (guid)
        ds (get-datastore {:host "http://localhost:8123"
                           :project-id "some-project"
                           :namespace "some-namespace"})
        kind (create-kind ds "kind")
        entity-key (create-key kind k)
        entity (create-entity entity-key {:text "text"})]
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
        entity-key1 (create-key kind1 (guid))
        entity-key2 (create-key kind2 (guid))
        entity1 (create-entity entity-key1 {:text "text"})
        entity2 (create-entity entity-key2 {:text "different text"})]
    (store-entity ds1 entity1)
    (store-entity ds2 entity2)
    (let [retrieved-entity1 (retrieve-entity ds1 entity-key1)
          retrieved-entity2 (retrieve-entity ds2 entity-key2)]
      (is (= (.getString retrieved-entity1 "text") "text"))
      (is (= (.getString retrieved-entity2 "text") "different text")))))

(deftest ^:acceptance can-store-and-retrieve-entity-with-many-data-fields
  (let [ds (get-datastore {:host "http://localhost:8123"
                           :project-id "some-project"
                           :namespace "some-namespace"})
        kind (create-kind ds "kind")
        entity-key (create-key kind (guid))
        entity (create-entity entity-key {:text "text"
                                          :bool true
                                          :double 1.2
                                          :int 2})]
    (store-entity ds entity)
    (let [retrieved-entity (retrieve-entity ds entity-key)]
      (is (= (.getString retrieved-entity "text") "text"))
      (is (= (.getBoolean retrieved-entity "bool") true))
      (is (= (.getDouble retrieved-entity "double") 1.2))
      (is (= (.getLong retrieved-entity "int") 2)))))
