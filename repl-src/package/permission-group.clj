(in-ns com.example.droid1.main)

(-> (.getPackageManager (*a))
    (.getAllPermissionGroups android.content.pm.PackageManager/GET_META_DATA)
    (->> (map permission-group-info))
    print-table)

;; |            getPermissionGroupInfo | android.content.pm.PermissionGroupInfo |                                                                           [java.lang.String int] |
;; |                 getPermissionInfo |      android.content.pm.PermissionInfo |                                                                           [java.lang.String int] |
