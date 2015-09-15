(ns mc.servlets)

(gen-class :name mc.core
           :extends javax.servlet.http.HttpServlet
           :impl-ns mc.core)

(gen-class :name mc.reloader
           :implements [javax.servlet.Filter]
           :impl-ns mc.reloader)
