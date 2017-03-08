(defproject astoria "0.1.1"
  :description "Google Cloud data store client"
  :url "https://github.com/mdaley/astoria"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.google.cloud/google-cloud-datastore "0.7.0" :exclusions [io.grpc/grpc-core
                                                                               io.netty/netty-codec-http2]
                  ]
                 [io.grpc/grpc-core "1.1.2"]
                 [io.netty/netty-codec-http2 "4.1.8.Final"]
                 [joda-time/joda-time "2.9.7"]]
  :scm {:name "git"
        :url "https://github.com/mdaley/astoria"}
  :repositories [["releases" {:url "https://clojars.org/repo"
                              :creds :gpg}]]
  :test-selectors {:default (complement (or :acceptance :integration))
                   :acceptance :acceptance
                   :integration :integration})
