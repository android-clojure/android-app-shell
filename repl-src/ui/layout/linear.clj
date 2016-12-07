(in-ns com.example.droid1.main)

;;; レイアウトの確認だけするなら
;;; Android Studio で Java なしで XML だけ弄って
;;; プレビューする方がいいかも
;;; 実際にはコードで弄らないだろうし、確認もしやすいし
;;;
;;; ただ、どういうXMLの属性と、それの取り得る値は知りたい
;;; http://tools.oesf.biz/android-7.0.0_r1.0/xref/frameworks/base/core/res/res/values/attrs.xml
;;; http://androidxref.com/7.0.0_r1/xref/frameworks/base/core/res/res/values/attrs.xml
;;; が参考になるかも
;;;
;;; 以下とか見ればいいのかな
;;; http://qiita.com/Yuki_Yamada/items/eb4a25cedca2ff8078fd
;;; https://developer.android.com/guide/topics/ui/declaring-layout.html
;;; 画像がたくさんあるところ見るのがいいと思う

;;; LinearLayout
;;; 上に小さいボタンが左から並ぶ
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT)))))

;;; 横いっぱいのボタンが上に重なって A しか見えない
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT)))))

;;; 縦いっぱいのボタンが左から並ぶ
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT)))))

;;; 画面いっぱいに2つのボタンが重なって A しか見えない
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT)))))

;;;
;;; 数値を指定してみた
;;;
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams. 100 200))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams. 200 100)))))

;;;
;;; android.widget.LinearLayout$LayoutParams じゃなくて android.view.ViewGroup$LayoutParams を使ってみる
;;; → 効果は変わらなかった
;;;
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.view.ViewGroup$LayoutParams.
                                 android.view.ViewGroup$LayoutParams/WRAP_CONTENT
                                 android.view.ViewGroup$LayoutParams/FILL_PARENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.view.ViewGroup$LayoutParams.
                                 android.view.ViewGroup$LayoutParams/WRAP_CONTENT
                                 android.view.ViewGroup$LayoutParams/FILL_PARENT)))))

;;;
;;; 縦並びにしてみる
;;;

;;; 左に小さいボタンが上から並ぶ
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.setOrientation android.widget.LinearLayout/VERTICAL)
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT)))))

;;; 縦いっぱいのボタンが左端に寄って重なる。Aしか見えない
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.setOrientation android.widget.LinearLayout/VERTICAL)
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT)))))

;;; 横いっぱいのボタンが縦に並ぶ
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.setOrientation android.widget.LinearLayout/VERTICAL)
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/WRAP_CONTENT)))))

;;; 画面いっぱいに2つのボタンが重なって A しか見えない
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.LinearLayout. (*a))
                      (.setOrientation android.widget.LinearLayout/VERTICAL)
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "A"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT))
                      (.addView (doto (android.widget.Button. (*a))
                                  (.setText "B"))
                                (android.widget.LinearLayout$LayoutParams.
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT
                                 android.widget.LinearLayout$LayoutParams/FILL_PARENT)))))

;; AbsoluteLayout
;; AbsoluteLayout.LayoutParams
;; FrameLayout
;; FrameLayout.LayoutParams
;; Gallery
;; Gallery.LayoutParams
;; GridLayout
;; GridLayout.Alignment
;; GridLayout.LayoutParams
;; GridLayout.Spec
;; LinearLayout
;; LinearLayout.LayoutParams
;; RadioGroup
;; RadioGroup.LayoutParams
;; RelativeLayout
;; RelativeLayout.LayoutParams
;; TableLayout
;; TableLayout.LayoutParams
;; TableRow
;; TableRow.LayoutParams
;; Toolbar
;; Toolbar.LayoutParams
