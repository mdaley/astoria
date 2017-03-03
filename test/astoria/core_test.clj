(ns astoria.core-test
  (:require [clojure.test :refer :all]
            [astoria.core :refer :all])
  (:import [com.google.cloud
            ServiceOptions$Builder]
           [com.google.cloud.datastore
            BaseKey$Builder
            DatastoreOptions
            DatastoreOptions$Builder
            Datastore
            Entity
            KeyFactory]))

(defn- invoke-public-method-of-non-public-base-class
  [instance base-class method-name args-def args]
  (let [accessible-method (doto (.getDeclaredMethod base-class method-name (into-array args-def))
                            (.setAccessible true))]
    (.invoke accessible-method instance (into-array args))))

(defn- set-host
  [^DatastoreOptions$Builder builder ^String host]
  (invoke-public-method-of-non-public-base-class builder ServiceOptions$Builder
                                                 "setHost" [String] [host]))

(defn- set-project
  [^DatastoreOptions$Builder builder ^String project-id]
  (invoke-public-method-of-non-public-base-class builder ServiceOptions$Builder
                                                 "setProjectId" [String] [project-id]))

(defn- set-kind
  [^KeyFactory key-factory ^String kind]
  (invoke-public-method-of-non-public-base-class key-factory BaseKey$Builder
                                                 "setKind" [String] [kind]))

(defn- get-datastore
  [host project-id]
  (-> (DatastoreOptions/newBuilder)
      (set-host host)
      (set-project project-id)
      .build
      .getService))

(defn- create-kind
  [datastore kind]
  (set-kind (.newKeyFactory datastore) kind))

(defn- create-key
  [kind name]
  (.newKey kind name))

(defn- create-entity
  [key text]
  (.build (.set (Entity/newBuilder key) "text" text)))

(defn- store-entity
  [datastore entity]
  (.put datastore entity))

(defn- retrieve-entity
  [datastore key]
  (.get datastore key))

(deftest store-something
  (testing "I'm going to store something"
    (let [ds (get-datastore "http://localhost:8123" "project")
          kind (create-kind ds "kind")
          entity-key (create-key kind "key")
          entity (create-entity entity-key "text")]
      (store-entity ds entity)
      (let [retrieved-entity (retrieve-entity ds entity-key)]
        (is (= (.getString retrieved-entity "text") "text"))))))
