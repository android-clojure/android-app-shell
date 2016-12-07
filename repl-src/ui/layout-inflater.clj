(in-ns com.example.droid1.main)

;;; https://developer.android.com/reference/android/view/LayoutInflater.html#inflate(org.xmlpull.v1.XmlPullParser
;;; http://stackoverflow.com/questions/33331935/xmlpullattributes-cannot-be-cast-to-xmlblockparser
;;; 重いから、実行時にXMLの処理はしないで、コンパイル時にするってコメントにあった
;;; という事で、実行時にXML作れないみたい
(-> (*a)
    (.getSystemService android.content.Context/LAYOUT_INFLATER_SERVICE)
    (.inflate (-> (org.xmlpull.v1.XmlPullParserFactory/newInstance)
                  #_(doto (.setValidating true))
                  .newPullParser
                  (doto (.setInput (java.io.StringReader.
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>
<ProgressBar android:id=\"@+id/progressbar_horizontal\"
        android:layout_height=\"wrap_content\"
        android:layout_width=\"fill_parent\"
        android:indeterminate=\"false\"
        style=\"?android:attr/progressBarStyleHorizontal\" />"))))
              nil))
;;; ClassCastException android.util.XmlPullAttributes cannot be cast to android.content.res.XmlBlock$Parser  android.content.res.Resources$Theme.obtainStyledAttributes (Resources.java:1262)
;;; ClassCastException って、何の為に interface って機能があるか? と思う
;;; とにかく、文字列からじゃなくて事前にXMLをコンパイルして apk に入れておかないと
;;; 難しい事っていうのがあるみたい
;;; reify で XmlPullAttributes 作ってもダメそう


;;; これは意味あるのか?
(-> (org.xmlpull.v1.XmlPullParserFactory/newInstance)
    (doto (.setValidating true)) ; これの有無で例外出たり出なかったり
    .newPullParser)
;;; XmlPullParserException could not create parser: org.kxml2.io.KXmlParser: org.xmlpull.v1.XmlPullParserException: unsupported feature: http://xmlpull.org/v1/doc/features.html#validation (position:START_DOCUMENT null@1:1) ;   org.xmlpull.v1.XmlPullParserFactory.newPullParser (XmlPullParserFactory.java:197)



;;; inflate の引数で挙動が違うという話
;;; http://y-anz-m.blogspot.jp/2012/04/android-viewgroup.html
;;; http://qiita.com/Bth0061/items/c4f66477979d064913e4
;;; 試してみないとよく分からない
