(in-ns com.example.droid1.main)

;;; context
;;; http://stackoverflow.com/questions/6854265/getapplicationcontext-getbasecontext-getapplication-getparent

(*a)
;; => #object[com.example.droid1.MainActivity 0x9ee4bc80 "com.example.droid1.MainActivity@9ee4bc80"]

(.getBaseContext (*a))
;; => #object[android.app.ContextImpl 0x9ee7d8b0 "android.app.ContextImpl@9ee7d8b0"]
;; Activity と同じ寿命らしい

(.getApplication (*a))
;; => #object[neko.App 0x9e78e3a0 "neko.App@9e78e3a0"]

(.getApplicationContext (*a))
;; => #object[neko.App 0x9e78e3a0 "neko.App@9e78e3a0"]



;;; ApplicationContext の ApplicationContext … とやっても変らない
(= (-> (*a) .getApplicationContext)
   (-> (*a) .getApplicationContext .getApplicationContext)
   neko.App/instance)
;; => true


;;; Application と Activity で共通のものも多いので、比較してみた

(.getApplicationContext (*a))
;; => #object[neko.App 0x9e709bc8 "neko.App@9e709bc8"]


(= (-> (*a) .getPackageName) ;=> "com.example.droid1"
   (-> (*a) .getApplicationContext .getPackageName))
;; => true

;;; dalvik.system.PathClassLoader
(= (-> (*a) .getClassLoader)
   ;; => #object[dalvik.system.PathClassLoader 0x9e7087e8
   ;; "dalvik.system.PathClassLoader[dexPath=/data/app/com.example.droid1-2.apk,
   ;;                                libraryPath=/data/app-lib/com.example.droid1-2]"]
   (-> (*a) .getApplicationContext .getClassLoader))
;; => true


;;; android.content.res.AssetManager
;;; Java のリソースみたいなの
(= (-> (*a) .getAssets)
   (-> (*a) .getResources .getAssets)
   (-> (*a) .getApplicationContext .getAssets))
;; => true


;;; android.os.Looper
;;; メインスレッド
;;; UIの処理は、このスレッドしかできない
(= (-> (*a) .getMainLooper)
   (-> (*a) .getApplicationContext .getMainLooper)
   (android.os.Looper/getMainLooper))
;; => true


;;; 以下よく分からないもの

;;; android.content.pm.ApplicationInfo
(= (.getApplicationInfo (*a))
   (-> (*a) .getApplicationContext .getApplicationInfo))
;; => true
(.describeContents (.getApplicationInfo (*a)))


;;; android.app.ContextImpl$ApplicationContentResolver
(= (-> (*a) .getContentResolver)
   (-> (*a) .getApplicationContext .getContentResolver))
;; => false


;;; android.app.ApplicationPackageManager
(= (-> (*a) .getPackageManager)
   (-> (*a) .getApplicationContext .getPackageManager))
;; => false


;;; android.content.res.Resources
(= (-> (*a) .getResources)
   (-> (*a) .getApplicationContext .getResources))
;; => true


;;; android.content.res.Resources$Theme
(= (-> (*a) .getTheme)
   (-> (*a) .getApplicationContext .getTheme))
;; => false


;;; 壁紙
;;; android.graphics.drawable.BitmapDrawable
(= (-> (*a) .getWallpaper)
   (-> (*a) .getApplicationContext .getWallpaper))
;; => false
;;; だったけど (-> (*a) .getWallpaper) を毎回やっても違うオブジェクトが返ってきているみたい

(= (-> (*a) .getWallpaperDesiredMinimumHeight) ; => 1920
   (-> (*a) .getApplicationContext .getWallpaperDesiredMinimumHeight))
;; => true

(= (-> (*a) .getWallpaperDesiredMinimumWidth) ; => 2108
   (-> (*a) .getApplicationContext .getWallpaperDesiredMinimumWidth))
;; => true


;;; ファイル・ディレクトリ
(= (-> (*a) .getCacheDir) ;=> ^java.io.File "/data/data/com.example.droid1/cache"
   (-> (*a) .getApplicationContext .getExternalCacheDir) ;=> nil
   )
;; => false

(= (-> (*a) .getExternalCacheDir) ;=> ^java.io.File
   (-> (*a) .getApplicationContext .getExternalCacheDir)
   nil)
;; => true

(= (-> (*a) .getFilesDir) ; ^java.io.File "/data/data/com.example.droid1/files"
   (-> (*a) .getApplicationContext .getFilesDir))
;; => true
;; (-> (*a) .getFilesDir .getAbsolutePath)

(= (-> (*a) .getObbDir) ; => ^java.io.File "/mnt/sdcard/Android/obb/com.example.droid1"
   (-> (*a) .getApplicationContext .getObbDir))
;; => true

;; 以下2つはなぜか java.io.File じゃない
(= (-> (*a) .getPackageCodePath) ; => "/data/app/com.example.droid1-2.apk"
   (-> (*a) .getApplicationContext .getPackageCodePath))
;; => true

(= (-> (*a) .getPackageResourcePath) ; => "/data/app/com.example.droid1-2.apk"
   (-> (*a) .getApplicationContext .getPackageResourcePath))
;; => true




;;; 以下 android.content.Context にあるけど使えなかった
;;; 継承先で private にしているのかも
;;; 使えないものは Application でも Activity でも使えなかった
;; (.getCodeCacheDir (*a))
;; (.getDataDir (*a))
;; (.getExternalCacheDirs (*a))
;; (.getExternalMediaDirs (*a))
;; (.getNoBackupFilesDir (*a))
;; (.getObbDirs (*a))

;; (-> (*a) .getApplicationContext .getCodeCacheDir)
;; (-> (*a) .getApplicationContext .getDataDir)
;; (-> (*a) .getApplicationContext .getExternalCacheDirs)
;; (-> (*a) .getApplicationContext .getExternalMediaDirs)
;; (-> (*a) .getApplicationContext .getNoBackupFilesDir)
;; (-> (*a) .getApplicationContext .getObbDirs)


#_(-> (*a)
      .getApplicationContext
      (.getDrawable android.R$drawable/ic_media_pause))
#_(-> (*a)
      (.getDrawable android.R$drawable/ic_media_pause))

;;; その他
(-> (*a)
    (.getSystemService android.content.Context/LAYOUT_INFLATER_SERVICE))
;; => #object[com.android.internal.policy.impl.PhoneLayoutInflater 0x9eca1bf8 "com.android.internal.policy.impl.PhoneLayoutInflater@9eca1bf8"]
(= (-> (*a)
       (.getSystemService android.content.Context/LAYOUT_INFLATER_SERVICE))
   (-> (*a)
       .getApplicationContext
       (.getSystemService android.content.Context/LAYOUT_INFLATER_SERVICE))) ;; => false


;;; 他のアプリの Context を取得
;;; http://android-note.open-memo.net/sub/system--get-other-app-context.html
(.createPackageContext (*a) "com.android.calculator2"
                       (bit-or android.content.Context/CONTEXT_IGNORE_SECURITY
                               android.content.Context/CONTEXT_INCLUDE_CODE))
