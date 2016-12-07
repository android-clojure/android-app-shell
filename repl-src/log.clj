(in-ns com.example.droid1.main)

(android.util.Log/d (str *ns*) "bar")
;;; $ adb logcat -v time
;;; とかすれば見れるけど
;;; $ lein droid forward-port
;;; を同時に実行できないみたい
;;; やると先に起動していた方を殺してしまう
;;;
;;; でも、先に出しておいたログを見る事はできる
