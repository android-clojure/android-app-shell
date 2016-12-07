(in-ns com.example.droid1.main)

;;; 被らなければ何でもいいみたい
;;; context とコレの組合せで、一意になればいいみたい
(def notification-id 100)

;;; android.app.NotificationManager
(.getSystemService (*a) android.content.Context/NOTIFICATION_SERVICE)

;;; 画面の上の方に通知が来て貯まる
(let [context (*a)]
  (-> (.getSystemService context android.content.Context/NOTIFICATION_SERVICE)
      (.notify notification-id
               (-> (doto (android.app.Notification. R$drawable/ic_launcher
                                                    "You've got mail"
                                                    (System/currentTimeMillis))
                     (.setLatestEventInfo context
                                          "One new message"
                                          "FROM: foo@bar.com"

                                          ;; 何で Activity なのかが分からない
                                          ;; どこに Activity が出てきたのか?
                                          (android.app.PendingIntent/getActivity
                                           context 0
                                           (android.content.Intent. "my.package.VIEW_MAIL")
                                           0)))))))

;; android.app.PendingIntent/getActivity
;; の他に3種類ある
;;   PendingIntent/getBroadcast
;;   PendingIntent/getService
;;   PendingIntent/getActivities


;;; 通知を消す
(let [context (*a)]
  (-> (.getSystemService context android.content.Context/NOTIFICATION_SERVICE)
      (.cancel notification-id)))

;;; android.app.Notification の作成は
;;; ↓の方が互換性の面で多分いいけど、サポートライブラリが入ってないみたい
(-> (android.support.v4.app.NotificationCompat$Builder. (*a))
    (.setSmallIcon R$drawable/ic_launcher)
    (.setTicker "You've got mail")
    (.setWhen (System/currentTimeMillis))
    .build)

;;; これだと通知が出ない
(-> (.getSystemService (*a) android.content.Context/NOTIFICATION_SERVICE)
    (.notify notification-id
             (.build
              (doto (android.app.Notification$Builder. (*a))
                (.setContentTitle "Title")
                (.setContentText "content")
                (.setWhen (System/currentTimeMillis))
                ;; (.setCategory android.app.Notification/CATEGORY_ALARM)
                (.setPriority android.app.Notification/PRIORITY_DEFAULT)
                (.setDefaults android.app.Notification/DEFAULT_SOUND)
                (.setAutoCancel true)
                (.setContentIntent (android.app.PendingIntent/getActivity
                                    (*a) 0
                                    (android.content.Intent. "my.package.VIEW_MAIL")
                                    0))))))
