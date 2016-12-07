(in-ns com.example.droid1.main)

(let [sp (.getSharedPreferences (*a) "person"
                                ;; Use 0 or MODE_PRIVATE for the default operation
                                android.content.Context/MODE_PRIVATE)]
  (-> sp
      .edit
      (doto #_keep-indent
        (.putString "name" "foo")
        (.putInt "age" 80)
        .commit))
  [(.getString sp "name" "(no name)")
   (.getInt sp "age" -1)])
;;; 再インストール、アンインストールしたら消えるらしい
;;; アプリ内のストレージに保存されるからとの事
