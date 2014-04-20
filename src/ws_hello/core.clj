(ns ws-hello.core
  (:gen-class)
  (:require [org.httpkit.server :refer [with-channel on-close on-receive send! run-server]]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [ws-hello.middleware :refer [wrap-log wrap-cache-control]]))

(def readers (atom {}))
(def writers (atom {}))


(defn- add-ws-connection [store request]
  (with-channel request chan
    (swap! store assoc chan true)
    (println chan " connected")
    (on-close chan (fn [status]
                    (swap! store dissoc chan)
                    (println chan " disconnected. status: " status)))))

(defn add-reader-connection [request]
  (add-ws-connection readers request))

(defn add-writer-connection [request]
  (let [{chan :body :as response} (add-ws-connection writers request)]
    (on-receive chan
                (fn [message]
                  (println message)
                  (doseq [[rdr _] @readers]
                    (send! rdr message))))
    response))

(defapi app
  (with-middleware [wrap-log wrap-cache-control]
    (GET* "/"      [] (resource-response "index.html" {:root "public"}))
    (GET* "/read"  [] add-reader-connection)
    (GET* "/write" [] add-writer-connection)
    compojure.api.middleware/public-resource-routes
    (compojure.route/not-found "<h1>NO.</h1>")))

(defn -main [& args]
  (let [port (or (first args) 4040)]
    (println "Starting on port" port)
    (run-server #'app {:port (bigdec port)})))

;(def stop-server (-main))
;(stop-server)
