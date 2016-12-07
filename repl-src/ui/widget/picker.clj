(in-ns com.example.droid1.main)

;;; 用途が限定されているのが特徴だと思う

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.DatePicker. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.NumberPicker. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.TimePicker. (*a))
                      )))
