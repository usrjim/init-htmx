(ns usrj.htmx
  (:require [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn ping-handler [_]
  {:status 200, :body "<b>ok</b>"})

(def app
  (ring/ring-handler
   (ring/router
    [["/ping" {:get ping-handler}]])
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler))))

(defn -main []
  (run-jetty #'app {:port 8080, :join? false})
  (println "server running in port 8080"))

(comment
  (require '[ring.middleware.reload :refer [wrap-reload]])

  (def dev-server
    (run-jetty
     (-> #'app wrap-reload)
     {:port 8080, :join? false}))

  (.stop dev-server)
,)
