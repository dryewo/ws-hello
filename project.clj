(defproject ws-hello "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.1.18"]
                 [metosin/compojure-api "0.10.4"]
                 [javax.servlet/servlet-api "2.5"]]
  :main ^:skip-aot ws-hello.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
