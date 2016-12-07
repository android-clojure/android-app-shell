(in-ns com.example.droid1.main)

(let [uri (android.net.Uri/parse "http://example.com/foo/bar")]
  [(-> uri .getPathSegments)
   (-> uri
       .getPathSegments
       (.get 0))
   (-> uri
       .getPathSegments
       (.get 1))
   (-> uri
       .getLastPathSegment)])
;; => [["foo" "bar"] "foo" "bar" "bar"]


;;; https://developer.android.com/guide/topics/providers/content-provider-creating.html#ContentURI
(let [base "com.example.droid1.provider"
      um   (doto (android.content.UriMatcher. android.content.UriMatcher/NO_MATCH)
             ;; ↓の最後の引数の整数は適当に決めていい
             ;; Java の switch-case で分岐する為の数値
             (.addURI base "a" 1)
             (.addURI base "b" 2)
             (.addURI base "b/#" 3) ;# は数値にマッチする
             (.addURI base "c" 4)
             (.addURI base "c/*" 4) ;* は任意の文字にマッチ
             )]
  [(.match um (android.net.Uri/parse "content://com.example.droid1.provider/a"))
   (.match um (android.net.Uri/parse "content://com.example.droid1.provider/b"))
   (.match um (android.net.Uri/parse "content://com.example.droid1.provider/b/1000"))
   (.match um (android.net.Uri/parse "content://com.example.droid1.provider/c"))
   (.match um (android.net.Uri/parse "content://com.example.droid1.provider/c/aaa"))
   (.match um (android.net.Uri/parse "content://com.example.droid1.provider/d"))])
;; => [1 2 3 4 4 -1]


;;; content URI
;;; 整数の主キーを使って、REST の URL みたいな 感じになる事を想定しているみたいだけど
;;; 必ずしもそうする必用もないみたい
(android.content.ContentUris/withAppendedId android.provider.MediaStore$Images$Media/EXTERNAL_CONTENT_URI 38)
;; => #object[android.net.Uri$HierarchicalUri 0x9f0c11d0 "content://media/external/images/media/38"]

(android.content.ContentUris/withAppendedId android.provider.UserDictionary$Words/CONTENT_URI 4)
;; => #object[android.net.Uri$HierarchicalUri 0x9ec6c1f8 "content://user_dictionary/words/4"]
