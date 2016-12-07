(in-ns com.example.droid1.main)

;;; https://developer.android.com/guide/components/tasks-and-back-stack.html


;;; onNewIntent の処理を書換えておく
(swap! handlers assoc :on-new-intent
       (fn [saved-instance-state]
         (set-content-view! (*a)
                            (doto (android.widget.TextView. (*a))
                              (.setBackgroundColor (android.graphics.Color/rgb 0xff 0xff 0))
                              (.setText (str (.getClass (*a)) " new intent"))))))

;;; 一番上のActivityと同じのをさらに積もうとしても、積めない
;;; Activity は代らないけど onNewIntent で画面の書換えは起る
;;; でも、2回以上やっても同じ
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.MainActivity)
                       (.addFlags android.content.Intent/FLAG_ACTIVITY_SINGLE_TOP)))

;;; 別のを積む場合は影響なし
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.Activity1)
                       (.addFlags android.content.Intent/FLAG_ACTIVITY_SINGLE_TOP)))


(in-ns com.example.droid1.activity1)
;;; SINGLE_TOP は、その Activity が今一番上にあるか? だけを気にしているので
;;; 下に積んであっても、一番上じゃないなら、複数積む事はできる
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.MainActivity)
                       (.addFlags android.content.Intent/FLAG_ACTIVITY_SINGLE_TOP)))
