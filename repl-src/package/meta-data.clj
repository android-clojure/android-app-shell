(in-ns com.example.droid1.main)

;;; AndroidManifest の meta-data の取得
;;; https://developer.android.com/guide/topics/manifest/meta-data-element.html
;;; http://tomoyukim.hatenablog.com/entry/2014/08/06/092149

;;; meta-data の使い方
;;; http://stackoverflow.com/questions/38687159/what-is-metadata-and-what-is-the-use-of-it-in-android
;;; http://blog.iangclifton.com/2010/10/08/using-meta-data-in-an-androidmanifest/
;;; 別にコードに書いてもいいと思ってたけど…
;;; ライブラリで使うAPIのキーとか入れるといいとか言っている
;;; そうすれば、ライブラリの中身を知らなくても情報が渡せる様にできるので
;;; ダイナミックスコープ的なものと考えればいいかも
;;;
;;; でも、パッケージじゃなくて Activity とかコンポーネントの meta-data になると
;;; 自分で作っているものなので、そういう風には使えないなと思った
;;; ↓
;;; http://dev.classmethod.jp/smartphone/android/android-tips-33-taskstackbuilder-navutil/
;;; タスクのスタックを操作する場合に使うみたい
;;; → 他に方法はないと思うけど、情報が分散して良くないなと思った
;;; あとこのAPIはデータ作成と、IOがごっちゃになっていて良くない

;;; 自パッケージの application の meta-data を取る
(let [^android.os.Bundle b (-> (.getPackageManager (*a))
                               (.getApplicationInfo (.getPackageName (*a))
                                                    android.content.pm.PackageManager/GET_META_DATA)
                               .metaData)]
  (when b
    (manifest-meta-data b)))
;; => {"bar" 2, "foo" 1}

;;; 自パッケージの activity の meta-data を取る
(let [^android.os.Bundle b (-> (.getPackageManager (*a))
                               (.getActivityInfo (.getComponentName (*a))
                                                 android.content.pm.PackageManager/GET_META_DATA)
                               ;; ↑は↓でもいい
                               #_(.getActivityInfo (android.content.ComponentName. (*a) (.getClass (*a)))
                                                 android.content.pm.PackageManager/GET_META_DATA)
                               .metaData)]
  (when b
    (manifest-meta-data b)))
;; => {"bar" 20, "foo" 10}
;;; AndroidManifest に書いた通り Application のとキーは同じだけど、違う値が取れている
