(ns main.servlets)

(gen-class :name main.core
           :extends javax.servlet.http.HttpServlet
           :impl-ns main.core)

(gen-class :name main.reloader
           :implements [javax.servlet.Filter]
           :impl-ns main.reloader)
