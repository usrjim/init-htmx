(ns usrj.htmx
  (:require [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [ring.adapter.jetty :refer [run-jetty]]
            [rum.core :refer [render-static-markup]])
  (:gen-class))

(defn response-ok [data]
  {:status 200, :body (render-static-markup data)})

(defn ping-handler [_]
  (response-ok [:b "ok"]))

(def app
  (ring/ring-handler
   (ring/router
    [["/ping" {:get ping-handler
               :middleware [parameters-middleware]}]])
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler))))

(defn -main []
  (run-jetty #'app {:host "127.0.0.1" :port 8080, :join? false})
  (println "server running in port 8080"))

(comment
  (require '[ring.middleware.reload :refer [wrap-reload]])

  (def dev-server
    (run-jetty
     (-> #'app wrap-reload)
     {:host "127.0.0.1" :port 8080, :join? false}))

  (.stop dev-server))
,
