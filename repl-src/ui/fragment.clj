(in-ns com.example.droid1.main)

;;; transaction は必用なの?
;;; inflater じゃダメなの?
;;; inflater 使うと layout の XML は指定できて
;;; それを描画する事は出来るけど、
;;; その layout にボタンがあったりした場合に
;;; それに紐付いた処理を書く事が出来ない
;;; だから inflater だけだとフラグメントと言えないと思う
;;;
;;; フラグメントはGUIの部品なので、いろんなアクティビティから使う事が出来るはず

(.getSupportFragmentManager (*a))

;;; Fragment は View じゃないよ!
;;; ってエラーが出る!
(on-ui
 (set-content-view!
  (*a)
  (proxy [android.app.Fragment] []
    (onCreateView [^android.view.LayoutInflater inflater
                   ^android.view.ViewGroup container
                   ^android.os.Bundle bundle]
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
            (toast "clicked!" :short)))))))))

;;; みたいにクラス作ればいいのか?
;;; proxyでもいいけど
(.getClass
 (reify java.util.concurrent.Future
   ))
