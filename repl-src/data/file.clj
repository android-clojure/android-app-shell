(in-ns com.example.droid1.main)

(with-open [fos (.openFileOutput (*a)
                                 "test.txt"
                                 android.content.Context/MODE_PRIVATE)]
  (.write fos (.getBytes "hello world!")))
;;; (-> (*a) .getFilesDir)
;;; にファイルが出来るみたいだった


(require '[clojure.string])
(with-open [fis (.openFileInput (*a) "test.txt")
            r (java.io.InputStreamReader. fis)]
  (let [a (char-array 30)]
    (.read r a 0 30)
    (->> a
         (take-while #(not= 0 (int %)))
         clojure.string/join)))
;; => "hello world!"
