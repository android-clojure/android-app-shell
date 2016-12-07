(in-ns com.example.droid1.main)

;;; https://developer.android.com/guide/components/tasks-and-back-stack.html

;;; フラグなしなら、同じものをいくらでも積める
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.MainActivity))
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.Activity1))

;;; launchMode=singleTop
;;; 別のを積む場合は影響なし
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.Activity1)
                       (.addFlags (bit-or android.content.Intent/FLAG_ACTIVITY_CLEAR_TOP
                                          android.content.Intent/FLAG_ACTIVITY_NEW_TASK))))


android.content.Intent/FLAG_ACTIVITY_BROUGHT_TO_FRONT
android.content.Intent/FLAG_ACTIVITY_CLEAR_TASK
android.content.Intent/FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
android.content.Intent/FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
android.content.Intent/FLAG_ACTIVITY_FORWARD_RESULT
android.content.Intent/FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
android.content.Intent/FLAG_ACTIVITY_LAUNCH_ADJACENT
android.content.Intent/FLAG_ACTIVITY_MULTIPLE_TASK
android.content.Intent/FLAG_ACTIVITY_NEW_DOCUMENT
android.content.Intent/FLAG_ACTIVITY_NO_ANIMATION
android.content.Intent/FLAG_ACTIVITY_NO_HISTORY
android.content.Intent/FLAG_ACTIVITY_NO_USER_ACTION
android.content.Intent/FLAG_ACTIVITY_PREVIOUS_IS_TOP
android.content.Intent/FLAG_ACTIVITY_REORDER_TO_FRONT
android.content.Intent/FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
android.content.Intent/FLAG_ACTIVITY_RETAIN_IN_RECENTS
android.content.Intent/FLAG_ACTIVITY_TASK_ON_HOME

;;; 分かり易い様に単純な画面に変更もできる
@handlers
(swap! handlers assoc :on-create
       (fn [saved-instance-state]
         (set-content-view! (*a)
                            (doto (android.widget.TextView. (*a))
                              (.setBackgroundColor (android.graphics.Color/rgb 0x88 0x88 0))
                              (.setText (str (.getClass (*a))))))))
(swap! handlers assoc :on-new-intent
       (fn [saved-instance-state]
         (set-content-view! (*a)
                            (doto (android.widget.TextView. (*a))
                              (.setBackgroundColor (android.graphics.Color/rgb 0xff 0xff 0))
                              (.setText (str (.getClass (*a)) " new intent"))))))
(swap! handlers dissoc :on-create)


(in-ns com.example.droid1.activity1)


(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.MainActivity)
                       (.addFlags (bit-or android.content.Intent/FLAG_ACTIVITY_CLEAR_TOP
                                          android.content.Intent/FLAG_ACTIVITY_NEW_TASK))))

@handlers
(swap! handlers assoc :on-create
       (fn [saved-instance-state]
         (set-content-view! (*a)
                            (doto (android.widget.TextView. (*a))
                              (.setBackgroundColor (android.graphics.Color/rgb 0x88 0 0x88))
                              (.setText (str (.getClass (*a))))))))
(swap! handlers assoc :on-new-intent
       (fn [saved-instance-state]
         (set-content-view! (*a)
                            (doto (android.widget.TextView. (*a))
                              (.setBackgroundColor (android.graphics.Color/rgb 0xff 0 0xff))
                              (.setText (str (.getClass (*a)) " new intent"))))))

(swap! handlers dissoc :on-create)
