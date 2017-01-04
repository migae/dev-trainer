(ns modules.core
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

(clojure.core/println "loading modules: modules.core")

(defn modules-docs []
  (swagger-docs
   "/modules/swagger.json"
   {:info {:title "migae modules"
           :description "Clojure interface to the GAE Modules API"}
    :tags [{:name "modules", :description "modules admin"}]}))

(defapi modules-api

  (swagger-ui "/modules"
              :swagger-docs "/modules/swagger.json")
  (modules-docs)

  {:formats [:edn]}

  (context* "/modules" []
            :tags ["modules"]

   (GET* "/:nm" [nm :as uri]
         :summary "a test api"
        (-> (r/response (pr-str (str "Sorry, " nm ", the modules module is still under construction.")))
            (r/status 200)))

   (route/not-found "URL not found")))

(servlet/defservice
  (-> (routes
       modules-api)
      wrap-params
      redn/wrap-edn-params
      ))
