(in-ns com.example.droid1.main)

;;; 何で単なるデータの入れ物の Bundle が android.os パッケージにあるのか?
;;; 多分、Androidのコンポーネント(https://developer.android.com/reference/android/content/ComponentName.html)
;;; は、OS一般でいうとプロセスになると思うけど、
;;; そのプロセス間通信(IPC)とか、OSレベルのやり取りに使うからだと思う。
;;; (Intent を使う場面が多いと思うけど、Intent の中に Bundle が入っている)
;;; Android を設計した人も最初の方に、こういうデータ型を考えたのかも

;;; primitive型を入れる
(let [b (doto (android.os.Bundle.)
          (.putString "s" "foo")
          (.putInt "i" 3)
          (.putBoolean "b" true))]
  [(.keySet b)
   (.getString b "s")
   (.getInt b "i")
   (.getBoolean b "b")])
;; => [#{"b" "s" "i"} "foo" 3 true]

(let [b (doto (android.os.Bundle.)
          (.putString "s" "foo")
          (.putInt "i" 3)
          (.putBoolean "b" true))]
  (->> (.keySet b)
       (map #(do [% (.get b %)]))
       (into {})))
;; => {"b" true, "s" "foo", "i" 3}
;;; get は java.lang.Object を返す
;;; https://developer.android.com/reference/android/os/BaseBundle.html#get(java.lang.String)
;;; という事は int じゃなくて Integer とかプリミティブラッパーになってしまう点に注意


;;; primitive型の他に Serializable を入れられる
;;; シリアライズしたいから Serializable のハズなので
;;; データが大きいと、シリアライズに時間がかかって遅くなると思う
(let [b (doto (android.os.Bundle.)
          (.putSerializable "date" (java.util.Date.))
          (.putSerializable "cal" (java.util.Calendar/getInstance))
          ;; 配列にすれば何でも入る
          ;; でも、上手くいかない気がする
          ;; 何の為に Serializable なのかと考えると
          ;; Android の OS から、アプリケーションへの介入できる点を多くする為だと思う
          ;; なので、IPCみたいのが必用になる、
          ;; すると使えるデータ型は1つのプログラム内より poor になってしまう
          ;; これはトレードオフ
          (.putSerializable "objs" (object-array [(Object.) (Object.) (Object.)]))
          )]
  [(.keySet b)
   (.getSerializable b "date")
   (.getSerializable b "cal")
   (seq (.getSerializable b "objs"))])
;; => [#{"objs" "date" "cal"} #inst "2016-11-26T02:16:58.900-00:00" #inst "2016-11-26T02:16:58.900+00:00" (#object[java.lang.Object 0x9ed1d230 "java.lang.Object@9ed1d230"] #object[java.lang.Object 0x9ee66550 "java.lang.Object@9ee66550"] #object[java.lang.Object 0x9eff0ea8 "java.lang.Object@9eff0ea8"])]

(let [b (doto (android.os.Bundle.)
          (.putInt "i" 3)
          )]
  [(.containsKey b "i")
   (.remove b "i")
   (.containsKey b "i")])
;; => [true nil false]

(let [b (doto (android.os.Bundle.)
          (.putString "s" "foo")
          (.putInt "i" 3)
          (.putBoolean "b" true))]
  [(.isEmpty b)
   (.remove b "i")
   (.isEmpty b)
   (.clear b)
   (.isEmpty b)])
;; => [false nil false nil true]

(-> (doto (android.os.Bundle.)
      (.putString "s" "foo")
      (.putInt "i" 3)
      (.putBoolean "b" true))
    .keySet)
;; => #{"b" "s" "i"}
