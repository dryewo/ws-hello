(ns ws-hello.middleware)

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

