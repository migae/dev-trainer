(ns ds.core
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log :only [trace debug info]]
            [compojure.api.sweet :refer :all]
            ;; [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.handler.dump :refer :all] ; ring-devel
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.util.http-response :refer :all]
            [ring.util.response :as r]
            [ring.util.servlet :as servlet]
            [migae.datastore :as ds]
            ))

(println "loading ds.core")

(defn ds-docs []
  (swagger-docs
   "/ds/swagger.json"
   {:info {:title "migae datastore"
           :description "Clojure interface to the GAE Datastore API"}
    :tags [{:name "foo", :description "main foo stuff"}
           {:name "ctors", :description "entity-map constructors"}
           {:name "frob", :description "main frobnication"}]}))

(defapi ds-api
  (swagger-ui "/ds"
              :swagger-docs "/ds/swagger.json")
  (ds-docs)

  {:formats [:edn]}

  (context* "/foo" []
    :tags ["foo"]

    (GET* "/bar" []
         (str (format "<h1>FOO BAR</h1>")
              "\n\n<a href='/'>blah blah</a>"))

    (GET* "/baz" []
         (str (format "<h1>FOO BAZ</h1>")
              "\n\n<a href='/'>blah blah</a>"))

    (route/not-found "<h1>Page not found</h1>"))

  (context* "/frob" []
    :tags ["frob"]

    (GET* "/nicate" []
         (str (format "<h1>Frobnication!</h1>")
              "\n\n<a href='/'>blah blah</a>"))

    (route/not-found "<h1>Page not found</h1>"))

  (context* "/ds/ctor" []
    :tags ["ctors"]

    (GET* "/entity-map" []
          :summary "local constructor"
          (ok "local ctor: entity-map"))

    (GET* "/entity-map!" []
          :summary "push constructor"
          (ok "push ctor: entity-map!"))

    (GET* "/entity-map*" []
          :summary "pull constructor"
          (ok "pull ctor: entity-map*"))

    (route/not-found "<h1>Page not found</h1>")))

(servlet/defservice
  (-> #'ds-api
      wrap-params
      wrap-file-info
      ))


