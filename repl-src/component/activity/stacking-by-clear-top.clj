(require 'com.example.droid1.activity1)
(require 'com.example.droid1.activity2)

;;; https://developer.android.com/guide/components/tasks-and-back-stack.html


;;; 実験1
(in-ns com.example.droid1.main)
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.Activity1))

(in-ns com.example.droid1.activity1)
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.Activity2))

(in-ns com.example.droid1.activity2)
;;; MainActivity は既に積んであるので
;;; さらに上に積むんじゃなくて、今積んであるのを下して
;;; MainActivity を一番上にする
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.MainActivity)
                       (.addFlags android.content.Intent/FLAG_ACTIVITY_CLEAR_TOP)))
;;; この次にbackボタンを押すと、ホームアプリに戻る


;;; 実験2
(in-ns com.example.droid1.main)
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.Activity1))

(in-ns com.example.droid1.activity1)
(.startActivity (*a) (android.content.Intent. (*a) com.example.droid1.Activity2))

(in-ns com.example.droid1.activity2)
;;; Activity1 も既に積んであるので、さらに積むんじゃなくて戻る
(.startActivity (*a) (doto (android.content.Intent. (*a) com.example.droid1.Activity1)
                       (.addFlags android.content.Intent/FLAG_ACTIVITY_CLEAR_TOP)))
;;; ここから2回戻ったら ホームアプリになる
