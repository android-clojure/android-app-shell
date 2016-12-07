(in-ns com.example.droid1.main)

;;; android.os.Looper はイベントディスパッチして
;;; 適当な android.os.Handler にそれを処理させるものみたい
;;; なので java.lang.Thread は、これらよりもっと低レベルなもの

;;; 多分 neko.threading/on-ui と同じ様な働き
(-> (*a)
    .getMainLooper
    android.os.Handler.
    (.post (fn []
             (toast "foo" :short))))

;;; post 先の Looper を指定して Handler を作成
(-> (android.os.Looper/getMainLooper)
    android.os.Handler.
    (.post (fn []
             (toast "foo" :short))))

(.runOnUiThread (*a)
                (fn []
                  (toast "foo" :short)))

;;; HandlerThread, Looper, Handler の関係がよく分からん
(def ht (atom (android.os.HandlerThread. "foo")))

(.start @ht)
(let [service-looper  (.getLooper @ht)
      service-handler (android.os.Handler. service-looper)]
  (.post service-handler
         (fn []
           (toast "foo" :short))))

(.quit @ht)
(ns-unmap *ns* 'ht)


;;; http://d.hatena.ne.jp/kaw0909/20110410/1302418486
;;; Looper を指定しないで Handler の作成
;;; (android.os.Handler.)
;;; これをこのまま実行すると実行時エラーになる
;;;   RuntimeException Can't create handler inside thread that has not called Looper.prepare()  android.os.Handler.<init> (Handler.java:197)
;;; Java だと問題ないみたい
;;; Java というかコンパイルすればいいのかもしれないが
