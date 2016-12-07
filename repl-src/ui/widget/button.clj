(in-ns com.example.droid1.main)

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.Button. (*a))
                      (.setText "foo")
                      (.setTextColor
                       (android.content.res.ColorStateList.
                        (into-array [(int-array [android.R$attr/state_pressed])
                                     (int-array [android.R$attr/state_enabled])])
                        (int-array [android.graphics.Color/RED
                                    android.graphics.Color/GREEN])))
                      (.setOnClickListener
                       (reify android.view.View$OnClickListener
                         (^void onClick [this ^android.view.View view]
                          (toast "clicked!" :short)))))))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.CompoundButton. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.ImageButton. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.RadioButton. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.ToggleButton. (*a))
                      )))
(on-ui
 (set-content-view! (*a)
                    ;; ZoomButtonsController
                    (doto (android.widget.ZoomButton. (*a))
                      )))

;; 音量調節のボタン
(on-ui
 (set-content-view! (*a)
                    (doto (android.app.MediaRouteButton. (*a))
                      )))
