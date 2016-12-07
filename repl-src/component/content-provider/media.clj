(in-ns com.example.droid1.main)

;;; 画像のビットマップを取得
(let [^android.database.Cursor c
      (-> (*a)
          .getContentResolver
          (.query android.provider.MediaStore$Images$Media/EXTERNAL_CONTENT_URI ;/sdcard
                  #_android.provider.MediaStore$Images$Media/INTERNAL_CONTENT_URI ;端末内
                  nil nil nil nil))]
  (.moveToFirst c)
  ;;   画像の id を取得
  (->> (repeatedly (.getCount c)
                   #(let [x (.getLong c (.getColumnIndex c android.provider.MediaStore$Images$Media/_ID))]
                      (.moveToNext c)
                      x))
       ;; id を Uri に変換
       (map #(android.content.ContentUris/withAppendedId android.provider.MediaStore$Images$Media/EXTERNAL_CONTENT_URI %))
       ;; Uri を bitmap に変換
       (map (fn [^android.net.Uri uri]
              (-> (*a)
                  .getContentResolver
                  (android.provider.MediaStore$Images$Media/getBitmap uri))))))


#_(-> (*a)
    .getContentResolver
    (.query x
            (into-array String [android.provider.MediaStore$Images$Media/DATA])
            nil nil nil)
    )
