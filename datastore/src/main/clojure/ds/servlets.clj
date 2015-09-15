(ns ds.servlets)

(gen-class :name ds.core
           :extends javax.servlet.http.HttpServlet
           :impl-ns ds.core)

(gen-class :name ds.reloader
           :implements [javax.servlet.Filter]
           :impl-ns ds.reloader)
