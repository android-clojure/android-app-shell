(in-ns com.example.droid1.main)

;;; https://developer.android.com/guide/topics/providers/content-provider-basics.html?hl=ja
;;; で説明してあるものを操作
;;; リファレンス:
;;;   https://developer.android.com/reference/android/provider/UserDictionary.html
;;;   https://developer.android.com/reference/android/provider/UserDictionary.Words.html
;;; AndroidManifest に以下を入れておく事
;;;   <uses-permission android:name="android.permission.READ_USER_DICTIONARY"/>
;;;   <uses-permission android:name="android.permission.WRITE_USER_DICTIONARY"/>

(let [c (-> (*a)
            .getContentResolver
            (.query android.provider.UserDictionary$Words/CONTENT_URI
                    (into-array String [android.provider.UserDictionary$Words/_ID
                                        android.provider.UserDictionary$Words/WORD
                                        android.provider.UserDictionary$Words/APP_ID
                                        android.provider.UserDictionary$Words/FREQUENCY
                                        android.provider.UserDictionary$Words/LOCALE])

                    "(locale IS NULL) or (locale=?)"
                    ;; ↓が↑の ? に代入される
                    (into-array String [(str (java.util.Locale/getDefault))])

                    nil))
      cs (.getColumnNames c)]
  (.moveToFirst c)
  (repeatedly (.getCount c)
              #(let [x (zipmap cs
                               [(.getInt c 0)    ;_id
                                (.getString c 1) ;word
                                (.getString c 2) ;app_id
                                (.getInt c 3)    ;frequency
                                (.getString c 4) ;locale
                                ])]
                 (.moveToNext c)
                 x)))

(-> (*a)
    .getContentResolver
    (.insert android.provider.UserDictionary$Words/CONTENT_URI
             (doto (android.content.ContentValues.)
               (.put android.provider.UserDictionary$Words/APP_ID "example.user")
               ;; (.put android.provider.UserDictionary$Words/APP_ID "20")
               (.put android.provider.UserDictionary$Words/LOCALE "en_US")
               (.put android.provider.UserDictionary$Words/WORD "insert")
               (.put android.provider.UserDictionary$Words/FREQUENCY "100"))))
;;; なぜか appid が 0 で入る

(-> (*a)
    .getContentResolver
    (.update android.provider.UserDictionary$Words/CONTENT_URI
             (doto (android.content.ContentValues.)
               (.putNull android.provider.UserDictionary$Words/LOCALE))
             (str android.provider.UserDictionary$Words/LOCALE " LIKE ?")
             (into-array String ["en_%"])))

(-> (*a)
    .getContentResolver
    (.delete android.provider.UserDictionary$Words/CONTENT_URI
             (str android.provider.UserDictionary$Words/APP_ID " LIKE ?")
             (into-array String ["user"])))
;;; 全部消す
(-> (*a)
    .getContentResolver
    (.delete android.provider.UserDictionary$Words/CONTENT_URI
             nil nil))

;;; いつも思うけど、この手のインターフェースは劣化SQLでSQLには敵わない。
;;; やれる事を絞れば、使い易くて、出来る事が少ないものになって、
;;; 絞らなければ、SQLを使い難くしたもになる
;;; 現実にはその中間のものが出来る
