(ns usrj.htmx
  (:require [reitit.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [hiccup.core :refer [html]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]])
  (:gen-class))

(defn wrap-hiccup-render [handler]
  (fn [req]
    (let [response (handler req)
          body (if (vector? response)
                 (if (vector? (first response))
                   (apply (fn [& args] (html args)) response)
                   (html response))
                 response)]
      {:status 200
       :headers {"content-type" "text/html"}
       :body body})))

(defn wrap-api-defaults [handler]
  (-> handler
      wrap-hiccup-render
      (wrap-defaults api-defaults)))

(defn ping-handler [{{:keys [foo bar]} :params}]
  [:div
   [:b foo]
   [:i bar]])

(def app
  (ring/ring-handler
   (ring/router
    [["/ping" {:get ping-handler}]]
    {:data {:middleware [wrap-api-defaults]}})
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler))))

(defn -main []
  (run-server #'app {:ip "127.0.0.1" :port 8080})
  (println "server running in port 8080"))

(comment
  (require '[ring.middleware.reload :refer [wrap-reload]])

  (def dev-server
    (run-server
     (-> #'app wrap-reload)
     {:ip "127.0.0.1" :port 8080}))

  ;; stop server
  (dev-server)

  (require '[dev.nu.morse :as morse])
  (morse/launch-in-proc)
  (morse/inspect {:a 1 :b 2})

  ,)
