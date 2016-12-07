(in-ns com.example.droid1.main)

;;; https://developer.android.com/reference/android/util/AttributeSet.html
;;; This interface provides an efficient mechanism for retrieving data from compiled XML files
;;; とかあるので、普通のXML文字列は扱えないのかも
;;; 例外は起らないけど、値の取得に失敗する
(let [x (-> (android.util.Xml/newPullParser)
            (doto (.setInput (java.io.StringReader.
                              "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>
<Foo a=\"10\" b=\"20\" c=\"30\" />")))
            android.util.Xml/asAttributeSet)]
  [(.getAttributeIntValue x nil "a" 999)
   (.getAttributeCount x)])



;;; ここまでは問題ない
(-> (android.util.Xml/newPullParser)
    (doto (.setInput (java.io.StringReader.
                      "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>
<Foo a=\"10\" b=\"20\" c=\"30\" />")))
    android.util.Xml/asAttributeSet)


;;; http://stackoverflow.com/questions/4591696/how-do-i-generate-xml-resources-at-runtime-on-android
;;; この方法を使えば、XMLリソースを作ってそれを読めるかもしれない
;;; でもコメントにあるけど AssetManager 作るってどうやるんだ?
;;; Context から取る事はできたと思うけど
(android.content.res.AssetManager.)
;;; ドキュメントにないけど、デフォルトコンストラクタがあった
