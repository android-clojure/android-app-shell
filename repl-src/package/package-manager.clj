(in-ns com.example.droid1.main)
(require '[clojure.reflect :refer [reflect]]
         '[clojure.set :as set]
         '[clojure.pprint :refer [pprint print-table]])

;;; https://developer.android.com/reference/android/content/pm/PackageManager.html
;;; ざっくりいうと、
;;; インストールされている AndroidManifest.xml 全部が入った
;;; DBを検索をする機能だと思う。
;;; 検索結果は アプリだったり、その中のコンポーネントだったり。
;;; Android っていう OS がどういうものか分かる手掛かりになりそうな部分

;;; 定数の分類は以下を参照
;;; http://tools.oesf.biz/android-7.0.0_r1.0/xref/frameworks/base/core/java/android/content/pm/PackageManager.java#113
;;; 接頭語、接尾語では判断が付かない

;;;
;;; DB なので検索する為のアスペクトが色々ある
;;; - Component
;;;   - Activity | Service | Receiver | Provider
;;;   - .getActivityInfo | .getServiceInfo | .getReceiverInfo | .getProviderInfo
;;;     を使って情報が取れる
;;;   - 引数としてこれらを一意に指定する為に android.content.ComponentName が使われる
;;;   - 一意に指定する他にも、intent-filter の機能を使って条件に合うものを探す事も出来る
;;;     queryBroadcastReceivers | queryContentProviders | queryInstrumentation | queryIntentActivities |
;;;     queryIntentActivityOptions | queryIntentContentProviders | queryIntentServices | queryPermissionsByGroup
;;; - Application
;;; - android.content.pm.ResolveInfo よく分からん
;;; - Package と Appliction の違いが分からん

;;; PackageManager の状態を変えるもの
;; | extendVerificationTimeout | void | [int int long] |

;;; コンポーネントの設定
;; | setComponentEnabledSetting | void |                                                          [android.content.ComponentName int int] |
;; | getComponentEnabledSetting |  int |                                                                  [android.content.ComponentName] |
;; |       addPreferredActivity | void | [android.content.IntentFilter int android.content.ComponentName<> android.content.ComponentName] |

;; | getUserBadgedDrawableForDensity | android.graphics.drawable.Drawable | [android.graphics.drawable.Drawable android.os.UserHandle android.graphics.Rect int] |
;; |               getUserBadgedIcon | android.graphics.drawable.Drawable |                           [android.graphics.drawable.Drawable android.os.UserHandle] |
;; |              getUserBadgedLabel |             java.lang.CharSequence |                                       [java.lang.CharSequence android.os.UserHandle] |

;; |       isPermissionRevokedByPolicy |                                boolean |                                                              [java.lang.String java.lang.String] |

;;; UID が同じとか調べるらしい、でもだとしたらどうなるのか?
;; | checkSignatures | int |                           [int int] |
;; | checkSignatures | int | [java.lang.String java.lang.String] |

;;; 戻り値が void で verify っておかしいと思う
;; | verifyPendingInstall | void | [int int] |

(defn manifest-meta-data [^android.os.Bundle x]
  (->> (.keySet x)
       (map #(do [% (.get x %)]))
       (into {})))

(deffield-inspector path-permission android.content.pm.PathPermission)

(defn application-info [^android.content.pm.ApplicationInfo i]
  (-> i
      (inspect-fields android.content.pm.ApplicationInfo)
      (update :flags #(keywordize-bitset % android.content.pm.ApplicationInfo #"\AFLAG_"))))

(defn activity-info [^android.content.pm.ActivityInfo i]
  (-> i
      (inspect-fields android.content.pm.ActivityInfo)
      (update :flags #(keywordize-bitset % android.content.pm.ActivityInfo #"\AFLAG_"))
      (update :configChanges #(keywordize-bitset % android.content.pm.ActivityInfo #"\ACONFIG_"))
      (update :screenOrientation (constant-mapper android.content.pm.ActivityInfo #"\ASCREEN_"))))

(defn service-info [^android.content.pm.ServiceInfo i]
  (-> i
      (inspect-fields android.content.pm.ServiceInfo)
      (update :flags #(keywordize-bitset % #"\AFLAG_"))))

(defn provider-info [^android.content.pm.ProviderInfo i]
  (-> i
      (inspect-fields android.content.pm.ProviderInfo)
      (update :pathPermissions #(map path-permission %))))

(defn resolve-info [^android.content.pm.ResolveInfo i]
  (cond-> (inspect-fields i android.content.pm.ResolveInfo)
    (:activityInfo i) (update :activityInfo activity-info)
    (:serviceInfo i)  (update :serviceInfo service-info)
    (:providerInfo i) (update :providerInfo provider-info)))

(deffield-inspector permission-info android.content.pm.PermissionInfo)
(deffield-inspector permission-group-info android.content.pm.PermissionGroupInfo)
(deffield-inspector instrumentation-info android.content.pm.InstrumentationInfo)
(deffield-inspector package-feature android.content.pm.FeatureInfo)

(defn package-signature [^android.content.pm.Signature sig]
  (.toChars sig))

(defn package-info [^android.content.pm.ResolveInfo i]
  (let [i            (inspect-fields i android.content.pm.PackageInfo)
        unavailables (keep (fn [[k v]]
                             (when (nil? v)
                               k))
                           i)]
    (-> (apply dissoc i unavailables)
        (update :firstInstallTime #(java.util.Date. %))
        (update :lastUpdateTime #(java.util.Date. %))
        (cond-> #_keep-indent
          (:applicationInfo i)       (update :applicationInfo application-info)

          (seq (:activities i))      (update :activities #(map activity-info %))
          (seq (:services i))        (update :services #(map service-info %))
          (seq (:providers i))       (update :providers #(map provider-info %))
          (seq (:receivers i))       (update :receivers #(map activity-info %)) ;なぜか ActivityInfo を使っていた

          (seq (:permissions i))     (update :permissions #(map permission-info %))
          (seq (:instrumentation i)) (update :instrumentation #(map instrumentation-info %))
          (seq (:reqFeatures i))     (update :reqFeatures #(map package-feature %))

          ;; 人が普通に読む情報じゃない
          ;; (seq (:signatures i)) (update :signatures #(map package-signature %))
          ))))
