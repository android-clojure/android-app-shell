(in-ns com.example.droid1.main)

;;; ListView
;; #1
(let [array-adapter (android.widget.ArrayAdapter. (*a) android.R$layout/simple_list_item_1)]
  (on-ui
   (set-content-view! (*a)
                      (doto (android.widget.ListView. (*a))
                        (.setAdapter array-adapter))))
  (.add array-adapter "1")
  (.add array-adapter "2")
  (.add array-adapter "3"))

;; #2
(def array-adapter
  (android.widget.ArrayAdapter. (*a)
                                android.R$layout/simple_list_item_1))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.ListView. (*a))
                      (.setAdapter array-adapter))))

(on-ui (.add array-adapter "foo"))

(ns-unmap *ns* 'array-adapter)
