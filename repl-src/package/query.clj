(in-ns com.example.droid1.main)

;;; 他を検索してパッケージから meta-data を取る
(-> (.getPackageManager (*a))
    (.queryIntentActivities (doto (android.content.Intent.)
                              (.setAction android.content.Intent/ACTION_SEND)
                              (.setType "text/plain")

                              ;; ↓暗黙的Intent の場合は必須
                              (.addCategory android.content.Intent/CATEGORY_DEFAULT))
                            (bit-or 0
                                    ;; GET_ が接頭してあるものは、各結果で使う情報
                                    ;; SQLだと SELECT の属性の並び
                                    ;; https://developer.android.com/guide/topics/manifest/meta-data-element.html
                                    android.content.pm.PackageManager/GET_META_DATA
                                    ;; https://developer.android.com/guide/topics/manifest/intent-filter-element.html
                                    ;; android.content.pm.PackageManager/GET_RESOLVED_FILTER
                                    ;; 多分 https://developer.android.com/guide/topics/manifest/uses-library-element.html
                                    ;; android.content.pm.PackageManager/GET_SHARED_LIBRARY_FILES


                                    ;; MATCH_ が接頭してあるものは、結果を絞る為のもの
                                    ;; SQLだと WHERE
                                    ;; android.content.pm.PackageManager/MATCH_DEFAULT_ONLY

                                    ;; 以下は多分APIレベルの影響で使えなかった
                                    ;; android.content.pm.PackageManager/MATCH_ALL
                                    ;; android.content.pm.PackageManager/MATCH_DISABLED_COMPONENTS
                                    ;; android.content.pm.PackageManager/MATCH_DISABLED_UNTIL_USED_COMPONENTS
                                    ;; android.content.pm.PackageManager/MATCH_DIRECT_BOOT_AWARE
                                    ;; android.content.pm.PackageManager/MATCH_DIRECT_BOOT_UNAWARE
                                    ;; android.content.pm.PackageManager/MATCH_SYSTEM_ONLY MATCH_UNINSTALLED_PACKAGES
                                    ))
    (->> (map resolve-info))
    pprint)

(-> (.getPackageManager (*a))
    (.queryIntentActivities (doto (android.content.Intent. android.content.Intent/ACTION_SEND)
                              (.setAction android.content.Intent/ACTION_SEND)
                              (.addCategory android.content.Intent/CATEGORY_DEFAULT)
                              (.setType "image/*"))
                            0)
    (->> (map resolve-info))
    pprint)

(-> (.getPackageManager (*a))
    (.queryIntentActivities (doto (android.content.Intent. android.content.Intent/ACTION_CALL)
                              (.setType "*/*"))
                            (bit-or 0
                                    android.content.pm.PackageManager/MATCH_DEFAULT_ONLY))
    (->> (map resolve-info))
    pprint)

(-> (.getPackageManager (*a))
    (.queryIntentActivities (doto (android.content.Intent.)
                              (.addCategory android.content.Intent/CATEGORY_BROWSABLE))
                            (bit-or 0
                                    android.content.pm.PackageManager/MATCH_DEFAULT_ONLY
                                    #_android.content.pm.PackageManager/GET_META_DATA))
    (->> (map resolve-info))
    pprint)


;;; .queryIntentActivityOptions()
;;; ちょっと使い方が分からなかった

;;; .queryIntentActivities() と同じ様なのに .queryIntentServices() があるけど
;;; Service は明示的 Intent しか使わないので、検索といっても複数引っかかる事はないハズ
;;; Intent に 対象の Service の class を指定する前に、それが存在するか確認する為に使うものかなと思う

(-> (.getPackageManager (*a))
    (.queryBroadcastReceivers (android.content.Intent. android.content.Intent/ACTION_BOOT_COMPLETED)
                              ;; ↓ AndroidManifest.xml の meta-data
                              android.content.pm.PackageManager/GET_META_DATA)
    (->> (map resolve-info))
    pprint)

;;; http://stackoverflow.com/questions/2001590/get-a-list-of-available-content-providers
(-> (.getPackageManager (*a))
    (.queryContentProviders nil
                            (android.os.Process/myUid)
                            android.content.pm.PackageManager/GET_META_DATA)

    ;; list all content-providers
    #_(.queryContentProviders nil
                              0         ;uid
                              android.content.pm.PackageManager/GET_META_DATA)
    (->> (map provider-info))
    pprint)

;;; .queryIntentContentProviders()
;;; これもよく分からない content-provider は Intent じゃなくて
;;; content:// の URI を使うんじゃないのか?

(-> (.getPackageManager (*a))
    (.queryPermissionsByGroup
     ;; 以下の内どれかを選ぶ nil だと全部
     ;; android.Manifest$permission_group/CALENDAR
     ;; android.Manifest$permission_group/CAMERA
     ;; android.Manifest$permission_group/LOCATION
     ;; android.Manifest$permission_group/MICROPHONE
     android.Manifest$permission_group/STORAGE
     ;; 使えなかった
     ;; android.Manifest$permission_group/CONTACTS
     ;; android.Manifest$permission_group/SENSORS
     ;; android.Manifest$permission_group/SMS
     ;; android.Manifest$permission_group/PHONE


     ;; 表示する情報の指定
     0
     ;; android.content.pm.PackageManager/GET_META_DATA ;指定できるのはコレだけ
     )
    (->> (map permission-info))
    pprint)

(-> (.getPackageManager (*a))
    (.queryInstrumentation
     ;; パッケージ名の文字列 nil なら全部
     "com.example.android.apis"

     ;; 表示する情報の指定
     0
     ;; android.content.pm.PackageManager/GET_META_DATA ;指定できるのはコレだけ
     )
    (->> (map instrumentation-info))
    pprint)




(comment
  (-> (.getPackageManager (*a))
      (.getActivityInfo (android.content.ComponentName. (.getApplication (*a))
                                                        (.getClass (*a)))
                        android.content.pm.PackageManager/GET_META_DATA)
      .metaData)

  (-> (.getPackageManager (*a))
      (.getActivityInfo (android.content.ComponentName. "com.example.droid1" "MainActivity")
                        android.content.pm.PackageManager/GET_META_DATA))

  (-> (.getPackageManager (*a))
      (.getServiceInfo (android.content.ComponentName. "com.android.browser" "com.android.browser.BrowserActivity")
                       android.content.pm.PackageManager/GET_META_DATA))
  (-> (.getPackageManager (*a))
      (.getServiceInfo (android.content.ComponentName. "com.android.browser" "BrowserActivity")
                       android.content.pm.PackageManager/GET_META_DATA))
  (-> (.getPackageManager (*a))
      (.getServiceInfo (android.content.ComponentName. "com.android.browser" ".BrowserActivity")
                       android.content.pm.PackageManager/GET_META_DATA))

  (-> (.getPackageManager (*a))
      (.getServiceInfo (android.content.ComponentName. "com.android.camera" "Camera")
                       android.content.pm.PackageManager/GET_META_DATA))

  (-> (.getPackageManager (*a))
      (.getServiceInfo (android.content.ComponentName. "com.android.settings" "Settings")
                       android.content.pm.PackageManager/GET_META_DATA))
  )


;; .queryIntentActivities とかと以下は何が違うのか?
;; | resolveActivity | android.content.pm.ResolveInfo | [android.content.Intent int] |
;; |  resolveService | android.content.pm.ResolveInfo | [android.content.Intent int] |
;; | resolveContentProvider | android.content.pm.ProviderInfo | [java.lang.String int] |
;; | resolveContentProvider |        android.content.pm.ProviderInfo |                                                                           [java.lang.String int] |
