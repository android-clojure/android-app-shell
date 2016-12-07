(in-ns com.example.droid1.main)
(require '[clojure.java.io :as io])

;;; android.content.res.AssetManager
;;; https://developer.android.com/reference/android/content/res/AssetManager.html
(-> (*a) .getAssets)


(= (-> (*a) .getAssets)
   (android.content.res.AssetManager/getSystem))
;; => false


;;; 読込み専用のデータファイルがある場所を抽象化したものっぽい
;;; Java のリソースみたいなものだと思う
;;;
;;; android.content.res.Resources と同じ感じもしたけど、こっちは型が決っているので
;;; android.content.res.AssetManager の方が柔軟かつ面倒って感じかなと思う
;;;
;;; ファイルなので、指定した名前のファイルがないとかの実行時例外が出ると思う
;;; android.content.res.Resources なら、
;;; id の指定に関しては、ないものを指定するとコンパイルエラーになるだろうけど


;;; top directory
(-> (*a) .getAssets (.list "") seq)
;; => ("data_readers.clj" "foo.txt" "images" "sounds" "webkit")

;;; subdirs
(-> (*a) .getAssets (.list "images") seq)
;; => ("android-logo-mask.png" "android-logo-shine.png")

(-> (*a) .getAssets (.list "sounds") seq)
;; => ("bootanim0.raw" "bootanim1.raw")

(-> (*a) .getAssets (.list "webkit") seq)
;; => ("android-weberror.png" "hyph_en_US.dic" "incognito_mode_start_page.html" "missingImage.png" "nullPlugin.png" "play.png" "textAreaResizeCorner.png" "togglePlugin.png" "youtube.html" "youtube.png")


;;; files
(with-open [is (-> (*a) .getAssets (.open "foo.txt"))
            r (io/reader is)]
  (slurp r))
;; => "hello world!\n1\n2\n3\n"

(with-open [is (-> (*a) .getAssets (.open "foo.txt"))]
  (slurp is))
;; => "hello world!\n1\n2\n3\n"


(with-open [is (-> (*a) .getAssets (.open "data_readers.clj"))
            r (io/reader is)]
  (slurp r))
;; => "{}"
