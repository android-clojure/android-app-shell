(in-ns com.example.droid1.main)

;;; https://developer.android.com/reference/android/content/pm/PackageManager.html のメソッド
;;; Application
;; |         getApplicationBanner | android.graphics.drawable.Drawable | [android.content.pm.ApplicationInfo] |
;; |         getApplicationBanner | android.graphics.drawable.Drawable |                   [java.lang.String] |
;; | getApplicationEnabledSetting |                                int |                   [java.lang.String] |
;; |           getApplicationIcon | android.graphics.drawable.Drawable |                   [java.lang.String] |
;; |           getApplicationIcon | android.graphics.drawable.Drawable | [android.content.pm.ApplicationInfo] |
;; |           getApplicationInfo | android.content.pm.ApplicationInfo |               [java.lang.String int] |
;; |          getApplicationLabel |             java.lang.CharSequence | [android.content.pm.ApplicationInfo] |
;; |           getApplicationLogo | android.graphics.drawable.Drawable |                   [java.lang.String] |
;; |           getApplicationLogo | android.graphics.drawable.Drawable | [android.content.pm.ApplicationInfo] |
;; |     getInstalledApplications |                     java.util.List |                                [int] |
;; |   getResourcesForApplication |      android.content.res.Resources | [android.content.pm.ApplicationInfo] |
;; |   getResourcesForApplication |      android.content.res.Resources |                   [java.lang.String] |
