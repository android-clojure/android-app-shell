(in-ns com.example.droid1.main)

;;; AndroidManifest に何も追記しなくても動いた
;;; 同じアプリ内だから?
;;; それとも .registerReceiver の IntentFilter の設定がその代りになっているのか?

;; register the receiver
;; どこでやるのが適切なのか? activity の onCreate だと何度も実行されたりしないのか?
(.registerReceiver (*a)
                   (com.example.droid1.Receiver.)
                   (doto (android.content.IntentFilter.)
                     ;; 普通は action には
                     ;; android.content.Intent/ACTION_* を指定する
                     ;;
                     ;; https://developer.android.com/guide/components/intents-filters.html#Building
                     ;; 普通独自の action を作る場合は、パッケージ名を接頭する事
                     (.addAction "MY_ACTION")))

;; send a message
(-> (*a)
    (.sendBroadcast (doto (android.content.Intent.)
                      (.putExtra "message" "foo")
                      (.setAction "MY_ACTION"))))



;;; proxy(匿名クラス)版
;;; 最初からClass をコンパイルしておかなくてもいい様なので
(.registerReceiver (*a)
                   (proxy [com.example.droid1.Receiver] []
                     (onReceive [^android.content.Context context
                                 ^android.content.Intent intent]
                       (let [^android.os.Bundle bundle (.getExtras intent)
                             ^String msg               (.getString bundle "message")]
                         (toast (str "received: " msg)
                                :short))))
                   (doto (android.content.IntentFilter.)
                     (.addAction "MY_ACTION2")))

(-> (*a)
    (.sendBroadcast (doto (android.content.Intent.)
                      (.putExtra "message" "Bar")
                      (.setAction "MY_ACTION2"))))

;;; BroadcastReceiver の場合 intent-filter も
;;; https://developer.android.com/reference/android/content/IntentFilter.html
;;; で書けるらしい
;;; https://developer.android.com/guide/components/intents-filters.html?hl=ja
