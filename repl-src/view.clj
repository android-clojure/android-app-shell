(in-ns com.example.droid1.main)

;;; id
(.getId (android.widget.TextView. (*a)))
;; => -1
(let [tv (doto (android.widget.TextView. (*a))
           (.setId 100))]
  (.getId tv))
;; => 100
;;; これが XML での android:id かどうかはよく分からない
