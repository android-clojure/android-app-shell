(in-ns com.example.droid1.main)

;; | getDrawable |    android.graphics.drawable.Drawable | [java.lang.String int android.content.pm.ApplicationInfo] |
;; |     getText |                java.lang.CharSequence | [java.lang.String int android.content.pm.ApplicationInfo] |
;; |      getXml | android.content.res.XmlResourceParser | [java.lang.String int android.content.pm.ApplicationInfo] |

;;; resid と application-info の取得方法が分からんけど
;;; これで他のパッケージのリソースが取れる

(let [pm (.getPackageManager (*a))]
  (-> (.getText pm "android" resid application-info)
      pprint))
