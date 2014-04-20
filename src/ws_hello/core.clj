(ns ws-hello.core
  (:gen-class)
  (:require [org.httpkit.server]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]))

(defn add-reader [])

(defn add-writer [])

(defn wrap-cache-control [handler]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Cache-control"] "no-cache"))))

(defn wrap-log [handler]
  (fn [request]
    (let [req-id (apply str (take 4 (repeatedly #(rand-nth "ABCDEF0123456789"))))]
      (println req-id
                (->> (:request-method request) name clojure.string/upper-case)
                (:uri request)
                (:params request))
      (let [response (handler request)]
        (println req-id
                  (:status response)
                  (:body response))
        response))))

(defapi app
  (with-middleware [wrap-log wrap-cache-control]
    (GET* "/"      [] (resource-response "index.html" {:root "public"}))
    (GET* "/read"  [] add-reader)
    (GET* "/write" [] add-writer)
    compojure.api.middleware/public-resource-routes
    (compojure.route/not-found "<h1>NO.</h1>")))

(defn -main [& args]
  (let [port (or (first args) 4040)]
    (println "Starting on port" port)
    (org.httpkit.server/run-server #'app {:port (bigdec port)})))

;(def stop-server (-main))
;(stop-server)
