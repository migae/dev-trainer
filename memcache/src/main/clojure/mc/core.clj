(ns mc.core
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

(clojure.core/println "loading memcache: mc.core")

(defn home-page []
  (html5 [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
          [:input {:name "file" :type "file" :size "20"}]
          [:input {:type "submit" :name "submit" :value "submit"}]]))

(defn stream [s]
  (java.io.PushbackReader.
   (java.io.InputStreamReader.
    (ByteArrayInputStream. s)
    "UTF-8")))

(defn read-stream [s]
  (clojure.edn/read
   {:eof :theend}
   s))

(defn mc-docs []
  (swagger-docs
   "/mc/swagger.json"
   {:info {:title "migae memcache"
           :description "Clojure interface to the GAE Memcache API"}
    :tags [{:name "memcache", :description "memcache functions"}
           #_{:name "users", :description "user admin"}]}))

(defapi worker-api

  (swagger-ui "/mc"
              :swagger-docs "/mc/swagger.json")
  (mc-docs)

  {:formats [:edn]}

  (context* "/mc" []
            :tags ["memcache"]

   (GET* "/:nm" [nm :as uri]
         :summary "do some work"
        (-> (r/response (pr-str (str "Sorry, " nm ", the memcache component is still under construction.")))
            (r/status 200)))

   ;; (rfn {m :request-method, u :uri, ctx :context,  pi :path-info :as req}
   ;;      ;; (dump/handle-dump req))
   ;;      (str "UNCAUGHT REQUEST:" \newline))

   (route/not-found "URL not found")))

(servlet/defservice
  (-> (routes
       worker-api)
      wrap-params
      redn/wrap-edn-params
      (wrap-multipart-params {:store (byte-array-store)})
      ))
