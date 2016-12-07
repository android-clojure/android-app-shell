(in-ns com.example.droid1.main)

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.AnalogClock. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.DigitalClock. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.TextClock. (*a))
                      )))
