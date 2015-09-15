(ns main.core
  (:import [java.lang IllegalArgumentException RuntimeException]
           [java.io InputStream ByteArrayInputStream]
           [org.apache.commons.io IOUtils]
           [org.apache.commons.fileupload.util Streams]
           [org.apache.commons.fileupload UploadContext
            FileItemIterator
            FileItemStream
            FileUpload])
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log :only [trace debug info]]
            [compojure.api.sweet :refer :all]
            [compojure.route :as route]
            [ring.handler.dump :refer :all] ; ring-devel
            [ring.middleware.edn :as redn]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.multipart-params :refer :all]
            [ring.middleware.multipart-params.byte-array :refer [byte-array-store]]
            [ring.util.http-response :refer :all]
            [ring.util.response :as r]
            [ring.util.servlet :as servlet]
            [hiccup.core]
            [hiccup.page :refer [html5]]
            ))

(clojure.core/println "loading main: main.core")

(defn main-docs []
  (swagger-docs
   "/main/swagger.json"
   {:info {:title "Migae Dev-trainger Main"
           :description "Default module for dev-trainer webapp"}
    :tags [{:name "users", :description "user admin"}]}))

(defapi main-api

  (swagger-ui "/main"
              :swagger-docs "/main/swagger.json")
  (main-docs)

  {:formats [:edn]}

  (context* "/main" []
            :tags ["main"]

   (GET* "/:nm" [nm :as uri]
         :summary "do some work"
        (-> (r/response (pr-str (str "Sorry, " nm ", the main module is still under construction.")))
            (r/status 200)))

   (route/not-found "URL not found")))

(servlet/defservice
  (-> (routes
       main-api)
      wrap-params
      redn/wrap-edn-params
      ))
