(ns astoria.core
  (:import [com.google.cloud
            ServiceOptions$Builder]
           [com.google.cloud.datastore
            BaseKey$Builder
            BooleanValue
            DatastoreOptions
            DatastoreOptions$Builder
            Datastore
            DoubleValue
            Entity
            KeyFactory
            LongValue
            NullValue
            StringValue
            Value]))

(defn- invoke-public-method-of-non-public-base-class
  [{:keys [instance base-class method-name args-def args]}]
  (let [accessible-method (doto (.getDeclaredMethod base-class method-name (into-array args-def))
                            (.setAccessible true))]
    (.invoke accessible-method instance (into-array args))))

(defn- set-host
  [^DatastoreOptions$Builder builder ^String host]
  (invoke-public-method-of-non-public-base-class {:instance builder
                                                  :base-class ServiceOptions$Builder
                                                  :method-name "setHost"
                                                  :args-def [String]
                                                  :args [host]}))

(defn- set-project
  [^DatastoreOptions$Builder builder ^String project-id]
  (invoke-public-method-of-non-public-base-class {:instance builder
                                                  :base-class ServiceOptions$Builder
                                                  :method-name "setProjectId"
                                                  :args-def [String]
                                                  :args [project-id]}))

(defn- set-kind
  [^KeyFactory key-factory ^String kind]
  (invoke-public-method-of-non-public-base-class {:instance key-factory
                                                  :base-class BaseKey$Builder
                                                  :method-name "setKind"
                                                  :args-def [String]
                                                  :args [kind]}))

(defn get-datastore
  [& [{:keys [host project-id namespace]}]]
  (-> (cond-> (DatastoreOptions/newBuilder)
        host (set-host host)
        project-id (set-project project-id)
        namespace (.setNamespace namespace))
      .build
      .getService))

(defn create-kind
  [datastore kind]
  (set-kind (.newKeyFactory datastore) kind))

(defn create-key
  [kind name]
  (.newKey kind name))

(defn store-entity
  [datastore entity]
  (.put datastore entity))

(defn retrieve-entity
  [datastore key]
  (.get datastore key))

(defprotocol Storable
  "Extensible protocol for mapping Clojure values to values that can be placed in entities."
  (store-value ^Value [this]))

(extend-protocol Storable
  Value (store-value [x] x)
  nil (store-value [x] (NullValue.))
  Boolean (store-value [x] (BooleanValue. x))
  Long (store-value [x] (LongValue. x))
  Double (store-value [x] (DoubleValue. x))
  Integer (store-value [x] (LongValue. x))
  Float (store-value [x] (DoubleValue. x))
  String (store-value [x] (StringValue. x)))

(defn- set-entity-values
  [entity-builder name-value-map]
  (doall (map #(.set entity-builder (name (first %1)) (store-value (second %1))) (seq name-value-map)))
  entity-builder)

(defn create-entity
  [key name-value-map]
  (-> (Entity/newBuilder)
      (.setKey key)
      (set-entity-values name-value-map)
      .build))
