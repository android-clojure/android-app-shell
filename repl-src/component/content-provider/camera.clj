(in-ns com.example.droid1.main)

;;; http://qiita.com/Yuki_Yamada/items/137d15a4e65ed2308787
;;; SecurityException Permission Denial!!!
;;; android intent camera
(.startActivity
 (*a)
 (doto (Intent. android.provider.MediaStore/ACTION_IMAGE_CAPTURE)
   (.putExtra android.provider.MediaStore/EXTRA_OUTPUT
              (-> (.getContentResolver (*a))
                  (.insert android.provider.MediaStore$Images$Media/EXTERNAL_CONTENT_URI
                           (doto (android.content.ContentValues.)
                             (.put android.provider.MediaStore$Images$Media/TITLE
                                   (str (System/currentTimeMillis) ".jpg"))
                             (.put android.provider.MediaStore$Images$Media/MIME_TYPE
                                   "image/jpeg")))))))
