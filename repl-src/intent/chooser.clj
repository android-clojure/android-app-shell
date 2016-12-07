(in-ns com.example.droid1.main)

(let [pm (.getPackageManager (*a))]
  (-> (doto (android.content.Intent.)
        (.addCategory android.content.Intent/CATEGORY_BROWSABLE))
      (.resolveActivity pm)))


(let [pm     (.getPackageManager (*a))
      intent (android.content.Intent. android.content.Intent/ACTION_VIEW)]
  (when (.resolveActivity intent pm)
    (.startActivity (*a)
                    (android.content.Intent/createChooser intent
                                                          "choose an app!"))))
