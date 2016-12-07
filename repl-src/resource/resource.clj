(in-ns com.example.droid1.main)
(require '[clojure.reflect :refer [reflect]])
;;; res/ 以下の XML とかで定義した値を取得する機能

(import com.example.droid1.R$bool)
(import com.example.droid1.R$my_float1)
(import com.example.droid1.R$plurals)
(import com.example.droid1.R$array)


;;; 名前は違うけど、値が重複しているものもある
(def ^:private resolve-typed-value-type
  (->> [[android.util.TypedValue/TYPE_ATTRIBUTE        :ATTRIBUTE]
        [android.util.TypedValue/TYPE_DIMENSION        :DIMENSION]
        [android.util.TypedValue/TYPE_FIRST_COLOR_INT  :FIRST_COLOR_INT]
        [android.util.TypedValue/TYPE_FIRST_INT        :FIRST_INT]
        [android.util.TypedValue/TYPE_FLOAT            :FLOAT]
        [android.util.TypedValue/TYPE_FRACTION         :FRACTION]
        [android.util.TypedValue/TYPE_INT_BOOLEAN      :INT_BOOLEAN]
        [android.util.TypedValue/TYPE_INT_COLOR_ARGB4  :INT_COLOR_ARGB4]
        [android.util.TypedValue/TYPE_INT_COLOR_ARGB8  :INT_COLOR_ARGB8]
        [android.util.TypedValue/TYPE_INT_COLOR_RGB4   :INT_COLOR_RGB4]
        [android.util.TypedValue/TYPE_INT_COLOR_RGB8   :INT_COLOR_RGB8]
        [android.util.TypedValue/TYPE_INT_DEC          :INT_DEC]
        [android.util.TypedValue/TYPE_INT_HEX          :INT_HEX]
        [android.util.TypedValue/TYPE_LAST_COLOR_INT   :LAST_COLOR_INT]
        [android.util.TypedValue/TYPE_LAST_INT         :LAST_INT]
        [android.util.TypedValue/TYPE_NULL             :NULL]
        [android.util.TypedValue/TYPE_REFERENCE        :REFERENCE]
        [android.util.TypedValue/TYPE_STRING           :STRING]]
       (into {})))


;;; XML と値の関係を確認したいなら
;;; aapt で Java を生成させてソースを確認するのでもいいかも
;;; いちいちコンパイルとか転送とかやるのは効率悪い
;;; でもこれだと、リソース id しか確認できない
;;;
;;; できれば android.content.res.Resources あたりだけでも
;;; pc の JVM で扱える様になれば Clojure とかインタプリタで操作できるので効率がいい
;;;
;;; jar ってリロードできるか知らないけど、
;;; 出来るなら
;;;   R.java作成 → コンパイル → リロード → 確認
;;; とかすればかなり効率がいい

;;; XML では id/foo とかで管理する事になる
;;; layout, activity, fragment とかの単位で
;;; 名前ローカルにしたいけど
;;; 全部アプリケーションに対してグローバルになるのが微妙だと思う

;; |                getValue |                                  void |                [int android.util.TypedValue boolean] |
;; |                getValue |                                  void |   [java.lang.String android.util.TypedValue boolean] |
;; |      getValueForDensity |                                  void |            [int int android.util.TypedValue boolean] |
;; |   getDrawableForDensity |    android.graphics.drawable.Drawable |                                            [int int] |

;; |          getStringArray |                    java.lang.String<> |                                                [int] |
;; |             getIntArray |                                 int<> |                                                [int] |
;; |            getTextArray |              java.lang.CharSequence<> |                                                [int] |

;;; データ型毎に取得する方法が違う
;; |                   :name |                          :return-type |                                     :parameter-types |
;; |-------------------------+---------------------------------------+------------------------------------------------------|
;; |            getAnimation | android.content.res.XmlResourceParser |                                                [int] |
;; |                getMovie |                android.graphics.Movie |                                                [int] |
;; |       getColorStateList |    android.content.res.ColorStateList |                                                [int] |

;; |            getDimension |                                 float |                                                [int] |
;; | getDimensionPixelOffset |                                   int |                                                [int] |
;; |   getDimensionPixelSize |                                   int |                                                [int] |

;; |             getFraction |                                 float |                                        [int int int] |
;; |         getQuantityText |                java.lang.CharSequence |                                            [int int] |
;; |                 getText |                java.lang.CharSequence |                                                [int] |
;; |                 getText |                java.lang.CharSequence |                         [int java.lang.CharSequence] |

(-> (*a) .getResources (.getBoolean R$bool/t))
(-> (*a) .getResources (.getBoolean R$bool/f))

(-> (*a) .getResources (.getInteger android.R$integer/config_longAnimTime)) ;; => 500
(-> (*a) .getResources (.getInteger android.R$integer/config_longAnimTime)) ;; => 500

(let [v (android.util.TypedValue.)]
  (-> (*a) .getResources
      (.getValue R$my_float1/float_test v false))
  [(.getFloat v) (resolve-typed-value-type (.type v))])

R$id/id_test

;;; Format 対応もある
(-> (*a) .getResources (.getString android.R$string/ok));; => "OK"
;;; .getText は styling 対応
(-> (*a) .getResources (.getText android.R$string/ok));; => "OK"


;;; Format 対応版もある
(map #(-> (*a) .getResources (.getQuantityString R$plurals/plurals_test %))
     [0 1 2 3 10])


(-> (*a) .getResources (.getText android.R$string/ok));; => "OK"

(map #(let [res (-> (*a) .getResources)]
        [(-> res (.getDimension %))
         (-> res (.getDimensionPixelOffset %))
         (-> res (.getDimensionPixelSize %))])
     [R$dimen/width_dp
      R$dimen/width_dp
      R$dimen/width_pt
      R$dimen/width_px
      R$dimen/width_mm
      R$dimen/width_in])

(-> (*a) .getResources (.getIntArray R$array/integers) seq)
;; => (0 1 10)


(let [a (-> (*a) .getResources (.obtainTypedArray R$array/untyped_array))]
  (let [r (map #(.getString a %) (range (.length a)))]
    (.recycle a)
    r))

(let [a (-> (*a) .getResources (.obtainTypedArray R$array/colors))]
  (let [r (map #(format "%x" (.getColor a % android.R$color/black))
               (range (.length a)))]
    (.recycle a)
    r))






;;; テーマの指定も出入るもの
(-> (*a) .getResources (.getColor android.R$color/black) (->> (format "%x"))) ;; => "ff000000"
;; |       getColorStateList |    android.content.res.ColorStateList |            [int android.content.res.Resources$Theme] |
;; |             getDrawable |    android.graphics.drawable.Drawable |            [int android.content.res.Resources$Theme] |
;; |   getDrawableForDensity |    android.graphics.drawable.Drawable |        [int int android.content.res.Resources$Theme] |


(-> (*a) .getResources (.getLayout android.R$layout/simple_list_item_1))




;;; Android のプラットフォームで用意されているものは android.R 以下にある
;;; android.R$color とか
android.R$color/black
;;; 自分で XML 作ったりするアプリケーション独自のものは R 以下で
;;; R$drawable とか
R$drawable/splash_background
;;; import して名前が短くなっているとかではない
;;;
;;; 他にも
com.android.internal.R$styleable
3;;; とかあって、これは
;;; [SDK_DIR]/platforms/android-N/data/res にあるらしい





;;; https://developer.android.com/reference/android/content/res/Resources.html#getIdentifier(java.lang.String,%20java.lang.String,%20java.lang.String)
;;; 定数を使わない方法もある
;;; コンパイルエラーも出ないので、必用ない気もするが、
(= android.R$color/black
   (-> (android.content.res.Resources/getSystem)
       (.getIdentifier "black"          ;属性名
                       "color"          ;型
                       "android"        ;名前空間
                       ))) ;; => true
;;; com.android.internal.R 以下の値を取る場合は、これじゃないとダメと
;;; 書いてあったりしたけど、この環境だと定数で取れている
;;; Android に変更があったのかもしれない
;;; 他には、一連の値をループで操作したい場合とかは使えるかもしれない



;;; 同じ値(ID)が取れる
(= android.R$color/black
   (-> (android.content.res.Resources/getSystem)
       (.getIdentifier "black" "color" "android"))
   (-> (*a)
       .getResources
       (.getIdentifier "black" "color" "android"))
   (-> (*a)
       .getApplicationContext
       .getResources
       (.getIdentifier "black" "color" "android"))) ;; => true
;;; かと思いきや
[(-> (*a)
     .getResources
     (.getIdentifier "splash_background" "drawable" "com.example.droid1"))
 (-> (*a)
     .getApplicationContext
     .getResources
     (.getIdentifier "splash_background" "drawable" "com.example.droid1"))
 (-> (android.content.res.Resources/getSystem)
     (.getIdentifier "splash_background" "drawable" "com.example.droid1"))]
;; => [2130837505 2130837505 0]
;;; getSystem だと取れないものもあった
;;; アプリケーション、サードパーティライブラリとか独自定義したものは、
;;; getSystem では取れないのかもしれない
(= (-> (*a) .getResources)
   (-> (*a) .getApplicationContext .getResources));; => true
(= (-> (*a) .getResources)
   (android.content.res.Resources/getSystem));; => false
;;; という関係だった





;; => true
;;; http://wakuworks.jugem.jp/?eid=175
;;; を見ると private なリソースがあるらしくて、.getIdentifier なら取得できるらしい
;;; なぜか、この環境だと getIdentifier じゃなくて普通に取れる
;;; バージョンが新しいから?

;;; 長いけどこういう書き方の方が汎用的かも
(-> (*a)
    .getResources
    (.getIdentifier "splashscreen" "layout" (-> (*a)
                                                str
                                                (str/split #"\.")
                                                butlast
                                                (->> (str/join ".")))))


;;; String, Boolean, Drawable とか型毎に getter があって
;;; 基本的に リソースid を渡せば取得できる
;;; でも、大体の場合 ウィジェットに リソースid を渡す様に出来ているので
;;; 直接プログラムで扱う場合はあまりないかも
(-> (*a)
    .getResources
    (.getDrawable R$drawable/splash_background))
(-> (*a)
    .getResources
    (.getDrawable android.R$drawable/ic_delete))

(-> (*a)
    .getResources
    (.getString R$string/touch_me))
;; => "Touch me"
(-> (*a)
    .getResources
    (.getString android.R$string/cancel))
;; => "Cancel"

(-> (*a)
    .getResources
    (.getText android.R$string/cancel))
;; => "Cancel"


;;; .getResources しなくても Context から取得できるデータ型もある
(-> (*a) (.getString android.R$string/cancel))
;; => "Cancel"
(-> (*a) (.getText android.R$string/cancel))
;; => "Cancel"


;;; フォーマット文字列
;;; http://y-anz-m.blogspot.jp/2011/03/android-xml.html
;;; https://developer.android.com/reference/java/util/Formatter.html
(-> (*a) (.getString R$string/your_input_fmt (into-array ["xyz"])))
;; => "Your input: xyz"
(-> (*a) (.getString R$string/format_test
                     (into-array Object ["ABC" 10 (java.util.Calendar/getInstance)])))
;; => "s:ABC, d:10, t:01:19:12"


;; view に関しては findViewById を使う
;; (.findViewById (*a) R$id/xxx)


;;; リソースのメタ情報
(-> (*a) .getResources (.getResourceName android.R$string/ok))
;; => "android:string/ok"
(-> (*a) .getResources (.getResourceEntryName android.R$string/ok))
;; => "ok"
(-> (*a) .getResources (.getResourcePackageName android.R$string/ok))
;; => "android"
(-> (*a) .getResources (.getResourceTypeName android.R$string/ok))
;; => "string"



;;; http://wakuworks.jugem.jp/?eid=175
;;; 動くけど、やっている事の意味が分からない
(on-ui
 (let [ll (android.widget.LinearLayout. (*a))]
   (.inflate (android.view.LayoutInflater/from (*a))
             (-> (android.content.res.Resources/getSystem)
                 (.getIdentifier "search_bar" "layout" "android"))
             ll)
   (set-content-view! (*a) ll)))

R$attr/hoge_int


;;; ..../android-sdk-X/build-tools/VER/aapt dump resources XXX.apk
;;; で見える値と一致する事を確認
[(map #(format "%x" %) [R$string/app_name
                        R$string/input_is_empty
                        R$string/your_input_fmt
                        R$string/touch_me])
 (map #(format "%x" %) [R$drawable/ic_launcher
                        R$drawable/splash_background
                        R$drawable/splash_circle
                        R$drawable/splash_droid
                        R$drawable/splash_hands])
 (map #(format "%x" %) [R$id/splash_droid
                        R$id/splash_circles
                        R$id/splash_droid_hands
                        R$id/splash_app_name])]
;;; これを見ると、リソースの種類事に開始する番号が違うのが分かる
;;; ビット毎に意味があるのかもしれない
;;; 中途半端な位置から始まるのは android.R とかと被らない様にする為かも


;;; よく分からないけど
;;; R.java にあるけど Unable to resolve symbol になる inner class もある
;; com.example.droid1.R$attr
;; com.example.droid1.R$xml
;; com.example.droid1.R$styleable

(->> com.example.droid1.R$xml/book
     reflect
     :members)

com.example.droid1.R$bool/isTablet
(->> com.example.droid1.R$bool
     reflect
     :members)

;; (import )

;;; 以下は問題ない
R$string
R$id
R$anim
R$layout
R$style

(constants R$anim)
(constants R$animator)
(constants R$array)
(constants R$color)
(constants R$integer)
(constants R$interpolator)
(constants R$layout)
(constants R$mipmap)
(constants R$string)
(constants R$styleable)
(constants R$transition)

com.example.droid1.R$bool

[com.example.droid1.R$anim
 com.example.droid1.R$array
 com.example.droid1.R$attr
 com.example.droid1.R$bool
 com.example.droid1.R$color
 com.example.droid1.R$dimen
 com.example.droid1.R$drawable
 com.example.droid1.R$id
 com.example.droid1.R$integer
 com.example.droid1.R$layout
 com.example.droid1.R$string
 com.example.droid1.R$style
 com.example.droid1.R$styleable]

[com.example.droid1.R$anim/splash_rotation
 com.example.droid1.R$array/difficultyLevel
 com.example.droid1.R$attr/Type1
 com.example.droid1.R$bool/falseRes
 com.example.droid1.R$color/failColor
 com.example.droid1.R$dimen/frac0perc
 com.example.droid1.R$drawable/black
 com.example.droid1.R$id/asset_cookie
 com.example.droid1.R$integer/reference
 com.example.droid1.R$layout/splashscreen
 com.example.droid1.R$string/app_name
 com.example.droid1.R$style/MyStyle
 com.example.droid1.R$styleable/EnumStyle
 ]


;;; http://qiita.com/okano-pankaku@github/items/dcd2780b93c1e34e922b
;; dimension の問題
