(in-ns com.example.droid1.main)
(require '[clojure.reflect :refer [reflect]]
         '[clojure.pprint :refer [pprint]])

;;; 以下2つはクラスで一般的にあるもの
;; Intent()                                                             Create an empty intent.
;; Intent(Intent o)                                                     Copy constructor.

;; Intent(String action)                                                Create an intent with a given action.

;;; ブラウザでいうと action が mime とアプリケーションの関連付けみたいな感じだと思う
;; Intent(String action, Uri uri)                                       Create an intent with a given action and for a given data url.

;;; 明示的Intentの場合は良く使われていると思う
;; Intent(Context packageContext, Class<?> cls)                         Create an intent for a specific component.

;;; 何で Class と Uri 両方指定するのかよく分からん
;; Intent(String action, Uri uri, Context packageContext, Class<?> cls) Create an intent for a specific component with a specified action and data.


(import android.net.Uri android.content.Intent)

;;; 以下、暗黙的インテント
(.startActivity (*a)
                (Intent. Intent/ACTION_VIEW
                         (Uri/parse "http://example.com")))

(.startActivity (*a)
                (doto (Intent. Intent/ACTION_DIAL)
                  (.setData (Uri/parse (str "tel:" "03-9999-9999")))))

(.startActivity (*a)
                (doto (Intent.)
                  (.setClassName "com.android.browser" "com.android.browser.BrowserActivity")))

;;; http://qiita.com/nein37/items/087ef3ab6a11d6bbf9e6
(.startActivity (*a)
                (doto (Intent. Intent/ACTION_DIAL)
                  (.setAction android.provider.Settings/ACTION_ACCESSIBILITY_SETTINGS)))

#_(.startActivity (*a)
                (doto (Intent. Intent/ACTION_DIAL)
                  (.setAction android.provider.Settings/ACTION_ADD_ACCOUNT)
                  (.putExtra android.provider.Settings/EXTRA_ACCOUNT_TYPES
                             (into-array ["com.google"]))))

(.startActivity (*a)
                (doto (Intent. Intent/ACTION_DIAL)
                  (.setAction android.provider.Settings/ACTION_AIRPLANE_MODE_SETTINGS)))

(.startActivity (*a)
                (doto (Intent. Intent/ACTION_DIAL)
                  (.setAction android.provider.Settings/ACTION_APN_SETTINGS)))

(comment
  (.startActivity (*a)
                  (doto (Intent.)
                    (.setClassName "com.android.camera" "Camera")))

  (.startActivity (*a)
                  (doto (Intent.)
                    (.setClassName "com.android.settings" "Settings"))))


;;; https://developer.android.com/guide/components/intents-filters.html#ExampleSend
;;; 暗黙的インテントは startActivity の前に
;;; resolveActivity で Intent を処理できるアクティビティが存在するか確認する
;;; やらないとアプリがクラッシュするらしい
;;; こんな感じ
(let [pm     (.getPackageManager (*a))
      intent (doto (android.content.Intent.)
               #_(.setAction  android.content.Intent/ACTION_VIEW)

               (.addCategory android.content.Intent/CATEGORY_APP_CALENDAR)

               #_(.setType "text/plain")
               #_(.setType "text/html")
               #_(.setType "image/jpeg")

               #_(.addFlags (bit-or 0
                                  android.content.Intent/FLAG_ACTIVITY_NEW_TASK
                                  ))
               )]
  (when (.resolveActivity intent pm)
    (.startActivity (*a)
                    (android.content.Intent/createChooser intent
                                                          "choose app!"))))
;;; でも調べたとしても直後に捌けるアクティビティがアンインストールされる可能性は 0 じゃないと思う



;;; パッケージ名から起動対象の Activity を検索
(defn- intent-inspector [^android.content.Intent intent]
  (-> intent
      (inspect-getters android.content.Intent)
      (update :Flags
              #(keywordize-bitset % android.content.Intent #"\AFLAG_"))))

(let [pkg-name "com.android.calculator2"
      pm     (.getPackageManager (*a))
      intent (.getLaunchIntentForPackage pm pkg-name)]
  (when intent
    (-> intent
        intent-inspector
        pprint)))
;;; http://shekeenlab.hatenablog.com/entry/2015/09/19/101925
;;; によると .getLaunchIntentForPackage は
;;;   http://androidxref.com/7.0.0_r1/xref/frameworks/base/core/java/android/app/ApplicationPackageManager.java#168
;;; mComponent だけ設定して mPackage を設定しない
;;;   http://androidxref.com/7.0.0_r1/xref/frameworks/base/core/java/android/content/Intent.java#4741
;;; とあるけど、今試している環境だと両方設定されていた
;;; APIレベルによって挙動が違うのかも
;;;
;;; 思うに Android の API に曖昧さがある事が原因だと思う。
;;; 上のブログでも、ソースコードレベルでは原因が特定しているけど、
;;; それがバグか仕様かは分からない
;;; だから実装がフラフラするんじゃないかと
;;;
;;; でも、それなりのスピードで、大きいものを作っているんだろうから
;;; こういうものなのかもしれない。
