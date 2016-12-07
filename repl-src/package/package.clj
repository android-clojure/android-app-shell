(in-ns com.example.droid1.main)

;;; https://developer.android.com/reference/android/content/pm/PackageManager.html のメソッド
;; |              getInstalledPackages |                 java.util.List |                    [int] |
;; |         getLaunchIntentForPackage |         android.content.Intent |       [java.lang.String] |
;; | getLeanbackLaunchIntentForPackage |         android.content.Intent |       [java.lang.String] |
;; |             getPackageArchiveInfo | android.content.pm.PackageInfo |   [java.lang.String int] |
;; |                    getPackageGids |                          int<> |       [java.lang.String] |
;; |                    getPackageGids |                          int<> |   [java.lang.String int] |
;; |                    getPackageInfo | android.content.pm.PackageInfo |   [java.lang.String int] |
;; |                     getPackageUid |                            int |   [java.lang.String int] |
;; |                 getPackagesForUid |             java.lang.String<> |                    [int] |
;; |     getPackagesHoldingPermissions |                 java.util.List | [java.lang.String<> int] |
;; |              getPreferredPackages |                 java.util.List |                    [int] |
;; |           getInstallerPackageName |               java.lang.String |       [java.lang.String] |


;;; どういうパッケージが入っているか調べる
(let [pm (.getPackageManager (*a))]
  (-> pm
      (.getInstalledPackages (bit-or 0 0 ;各パッケージのどういう情報を取るかを指定
                                     ;; https://developer.android.com/guide/topics/manifest/manifest-intro.html#filestruct
                                     ;; に出てくる要素と大体同じものが並んでいると思う

                                     ;; <application> 内の要素
                                     ;; android.content.pm.PackageManager/GET_ACTIVITIES
                                     ;; android.content.pm.PackageManager/GET_SERVICES
                                     ;; android.content.pm.PackageManager/GET_PROVIDERS
                                     ;; android.content.pm.PackageManager/GET_RECEIVERS

                                     ;; android.content.pm.PackageManager/GET_INTENT_FILTERS

                                     ;; <manifest>(トップレベル)の中の要素
                                     ;; android.content.pm.PackageManager/GET_PERMISSIONS
                                     ;; android.content.pm.PackageManager/GET_CONFIGURATIONS
                                     ;; android.content.pm.PackageManager/GET_INSTRUMENTATION

                                     ;; android.content.pm.PackageManager/GET_GIDS
                                     ;; android.content.pm.PackageManager/GET_SIGNATURES
                                     ;; android.content.pm.PackageManager/GET_META_DATA
                                     ;; android.content.pm.PackageManager/GET_SHARED_LIBRARY_FILES
                                     ;; android.content.pm.PackageManager/GET_URI_PERMISSION_PATTERNS
                                     ;; android.content.pm.PackageManager/GET_UNINSTALLED_PACKAGES

                                     ;; android.content.pm.PackageManager/MATCH_DISABLED_COMPONENTS
                                     ;; android.content.pm.PackageManager/MATCH_DISABLED_UNTIL_USED_COMPONENTS
                                     ;; android.content.pm.PackageManager/MATCH_UNINSTALLED_PACKAGES
                                     ))
      (->> #_keep-indent
           ;; ↓で、起動可なパッケージに限定
           #_(filter #(.getLaunchIntentForPackage pm (.packageName %)))

           (map package-info)
           #_(map (juxt :packageName :versionName)))
      pprint))


;;; パッケージ名の変換をするらしいけど、特に変化はない
(let [pm (.getPackageManager (*a))]
  (-> pm
      (.getInstalledPackages 0)
      ;; (->> (map package-info))
      (->> (keep #(try
                    (-> %
                        package-info
                        :packageName
                        vector
                        (->> (into-array String)
                             #_(.currentToCanonicalPackageNames pm)
                             (.canonicalToCurrentPackageNames pm)))
                    (catch Exception e))))
      pprint))
(let [pm (.getPackageManager (*a))]
  (seq (.currentToCanonicalPackageNames pm (into-array ["com.android.backupconfirm"]))))
(let [pm (.getPackageManager (*a))]
  (seq (.canonicalToCurrentPackageNames pm (into-array ["com.android.backupconfirm"]))))


;;; ??
(let [pm (.getPackageManager (*a))]
  ;; .getInstalledPackages の :publicSourceDir, :sourceDir にある情報を使う
  (-> (.getPackageArchiveInfo pm "/system/framework/framework-res.apk" 0)
      package-info
      pprint))


;;;
(-> (.getPackageManager (*a))
    ((juxt #(.getDefaultActivityIcon %)
           #(.isSafeMode %)
           #_#(.getPackageInstaller %)
           #(seq (.getSystemSharedLibraryNames %))))
    pprint)

;;;
(let [pm (.getPackageManager (*a))]
  (-> pm
      (.getInstalledPackages 0)
      (->> (keep #(try
                    (-> %
                        package-info
                        :applicationInfo
                        :uid)
                    (catch Exception e)))
           (map #(do [% (.getNameForUid pm %)])))
      pprint))

;;; パッケージの状態を変えるもの
;;; 引数の java.lang.String は package名の文字列
;;; これらの変更の有効期限はあるのか? 変更するって事は AndroidManifest.xml を書換えるって事なのか?
;;; でも、permission に関しては、要求と許可が別なので AndroidManifest.xml の書換えじゃないハズ
;; |           addPackageToPreferred |    void |                  [java.lang.String] |
;; |      removePackageFromPreferred |    void |                  [java.lang.String] |
;; |                   addPermission | boolean | [android.content.pm.PermissionInfo] |
;; |              addPermissionAsync | boolean | [android.content.pm.PermissionInfo] |
;; |                removePermission |    void |                  [java.lang.String] |
;; |    setApplicationEnabledSetting |    void |          [java.lang.String int int] |
;; |         setInstallerPackageName |    void | [java.lang.String java.lang.String] |
;; | clearPackagePreferredActivities |    void |                  [java.lang.String] |

;;; パッケージの情報
;; |                 checkPermission |     int | [java.lang.String java.lang.String] |
