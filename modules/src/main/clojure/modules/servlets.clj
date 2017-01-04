(ns modules.servlets)

(gen-class :name modules.core
           :extends javax.servlet.http.HttpServlet
           :impl-ns modules.core)

(gen-class :name modules.reloader
           :implements [javax.servlet.Filter]
           :impl-ns modules.reloader)
