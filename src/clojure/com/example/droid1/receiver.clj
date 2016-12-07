(ns com.example.droid1.receiver
  (:require [neko.notify :refer [toast]]))

;;; https://developer.android.com/reference/android/content/BroadcastReceiver.html

;;; 見た中では以下が一番シンプルな実装だった
;;; http://android.keicode.com/basics/services-communicate-broadcast-receiver.php

(gen-class
 :name com.example.droid1.Receiver
 :extends android.content.BroadcastReceiver)

(defn -onReceive
  [^android.content.BroadcastReceiver this
   ^android.content.Context context
   ^android.content.Intent intent]
  (let [^android.os.Bundle bundle (.getExtras intent)
        ^String msg               (.getString bundle "message")]
    ;; UIスレッドなので toast しても大丈夫
    (toast (str "received: " msg)
           :short)))

(comment
  ;; register the receiver
  ;; どこでやるのが適切なのか? activity の onCreate だと何度も実行されたりしないのか?
  (.registerReceiver (*a)
                     (com.example.droid1.Receiver.)
                     (doto (android.content.IntentFilter.)
                       (.addAction "MY_ACTION")))

  ;; send a message
  (-> (*a)
      (.sendBroadcast (doto (android.content.Intent.)
                        (.putExtra "message" "Hello, BroadCast!")
                        (.setAction "MY_ACTION"))))
  )
