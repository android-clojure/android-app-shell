(in-ns com.example.droid1.main)

;;; https://developer.android.com/guide/components/tasks-and-back-stack.html

;;; これだと今のタスクの一番上に電卓が来る
;;; 戻るボタンで戻れる
(.startActivity (*a)
                (doto (android.content.Intent.)
                  (.setAction android.content.Intent/ACTION_MAIN)
                  (.addCategory android.content.Intent/CATEGORY_APP_CALCULATOR)))

;;; これは、新しいタスクなので 戻るボタンで終了する
(.startActivity (*a)
                (doto (android.content.Intent.)
                  (.setAction android.content.Intent/ACTION_MAIN)
                  (.addCategory android.content.Intent/CATEGORY_APP_CALCULATOR)
                  (.setFlags android.content.Intent/FLAG_ACTIVITY_NEW_TASK)))

;;; 上2つを終了させないでそのままにすると
;;; オーバービュー(https://developer.android.com/guide/components/recents.html)
;;; で電卓が2つある事が確認できる
