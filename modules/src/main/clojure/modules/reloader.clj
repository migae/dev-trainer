(ns modules.reloader
  (:import (javax.servlet Filter FilterChain FilterConfig
                          ServletRequest ServletResponse))
  (:require [ns-tracker.core :refer :all]))

(defn -init [^Filter this ^FilterConfig cfg])

(defn -destroy [^Filter this])

;; NOTE: the paths to watch are relative to modules/ear/build/exploded-app
;; make them match what's in modules/ear/src/modules/application/META-INF/application.xml
;;  and modules/ear/src/modules/application/META-INF/application.xml
(def worker-namespaces (ns-tracker ["./modules-0.1.0/WEB-INF/classes/modules"]))

(defn -doFilter
  [^Filter this
   ^ServletRequest rqst
   ^ServletResponse resp
   ^FilterChain chain]
  (doseq [ns-sym (worker-namespaces)]
    (do
      (println "ns changed: " ns-sym (type ns-sym))
      (let [sym (if (symbol? ns-sym)
                  ns-sym
                  (last ns-sym))]
      (require sym
               :reload
               ;;:verbose
               ))))
  (.doFilter chain rqst resp))

(clojure.core/println "loading modules.reloader")
