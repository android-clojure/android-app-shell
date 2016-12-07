(ns com.example.droid1.service
  (:require [neko.notify :refer [toast]]))

;;; https://developer.android.com/reference/android/app/Service.html

;;; 見た中では以下が一番シンプルな実装だった
;;; http://android.keicode.com/basics/services-simple.php

(gen-class
 :name com.example.droid1.Service
 :extends android.app.Service
 :exposes-methods {onCreate  super_onCreate
                   onDestroy super_onDestroy})

(defn -onBind [^com.example.droid1.Service this
               ^android.content.Intent intent]
  (android.util.Log/d (str *ns*) ": onBind")
  nil)

(defn -onStartCommand [^com.example.droid1.Service this
                       ^android.content.Intent intent
                       flags
                       start-id]
  (android.util.Log/d (str *ns*) ": onStartCommand")
  android.app.Service/START_STICKY)

(defn -onCreate [^com.example.droid1.Service this]
  (android.util.Log/d (str *ns*) ": onCreate")
  (.super_onCreate this))

(defn -onDestroy [^com.example.droid1.Service this]
  (android.util.Log/d (str *ns*) ": onDestroy")
  (.super_onDestroy this))
