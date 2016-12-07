(in-ns com.example.droid1.main)

(require '[clojure.set :as set]
         '[clojure.reflect :refer [reflect]]
         '[clojure.pprint :refer [pprint]])

(->> [android.content.Context/ACCESSIBILITY_SERVICE
      android.content.Context/ACCOUNT_SERVICE
      android.content.Context/ACTIVITY_SERVICE
      android.content.Context/ALARM_SERVICE
      android.content.Context/APPWIDGET_SERVICE
      ;; android.content.Context/APP_OPS_SERVICE
      android.content.Context/AUDIO_SERVICE
      ;; android.content.Context/BATTERY_SERVICE
      ;; android.content.Context/BIND_EXTERNAL_SERVICE
      android.content.Context/BLUETOOTH_SERVICE
      ;; android.content.Context/CAMERA_SERVICE
      ;; android.content.Context/CAPTIONING_SERVICE
      ;; android.content.Context/CARRIER_CONFIG_SERVICE
      android.content.Context/CLIPBOARD_SERVICE
      android.content.Context/CONNECTIVITY_SERVICE
      ;; android.content.Context/CONSUMER_IR_SERVICE
      android.content.Context/DEVICE_POLICY_SERVICE
      android.content.Context/DISPLAY_SERVICE
      android.content.Context/DOWNLOAD_SERVICE
      android.content.Context/DROPBOX_SERVICE
      ;; android.content.Context/FINGERPRINT_SERVICE
      ;; android.content.Context/HARDWARE_PROPERTIES_SERVICE
      android.content.Context/INPUT_METHOD_SERVICE
      android.content.Context/INPUT_SERVICE
      ;; android.content.Context/JOB_SCHEDULER_SERVICE
      android.content.Context/KEYGUARD_SERVICE
      ;; android.content.Context/LAUNCHER_APPS_SERVICE
      android.content.Context/LAYOUT_INFLATER_SERVICE
      android.content.Context/LOCATION_SERVICE
      ;; android.content.Context/MEDIA_PROJECTION_SERVICE
      android.content.Context/MEDIA_ROUTER_SERVICE
      ;; android.content.Context/MEDIA_SESSION_SERVICE
      ;; android.content.Context/MIDI_SERVICE
      android.content.Context/NETWORK_STATS_SERVICE
      android.content.Context/NFC_SERVICE
      android.content.Context/NOTIFICATION_SERVICE
      android.content.Context/NSD_SERVICE
      android.content.Context/POWER_SERVICE
      ;; android.content.Context/PRINT_SERVICE
      ;; android.content.Context/RESTRICTIONS_SERVICE
      android.content.Context/SEARCH_SERVICE
      android.content.Context/SENSOR_SERVICE
      android.content.Context/STORAGE_SERVICE
      ;; android.content.Context/SYSTEM_HEALTH_SERVICE
      ;; android.content.Context/TELECOM_SERVICE
      android.content.Context/TELEPHONY_SERVICE
      ;; android.content.Context/TELEPHONY_SUBSCRIPTION_SERVICE
      android.content.Context/TEXT_SERVICES_MANAGER_SERVICE
      ;; android.content.Context/TV_INPUT_SERVICE
      android.content.Context/UI_MODE_SERVICE
      ;; android.content.Context/USAGE_STATS_SERVICE
      android.content.Context/USB_SERVICE
      android.content.Context/USER_SERVICE
      android.content.Context/VIBRATOR_SERVICE
      android.content.Context/WALLPAPER_SERVICE
      android.content.Context/WIFI_P2P_SERVICE
      android.content.Context/WIFI_SERVICE
      android.content.Context/WINDOW_SERVICE]
     (map (fn [s]
            (try
              [s (class (.getSystemService (*a) s))]
              (catch Exception e))))
     (into {})
     pprint)
#_{"window"           android.view.WindowManagerImpl,
   "vibrator"         android.os.SystemVibrator,
   "alarm"            android.app.AlarmManager,
   "wifi"             android.net.wifi.WifiManager,
   "servicediscovery" android.net.nsd.NsdManager,
   "input_method"     android.view.inputmethod.InputMethodManager,
   "activity"         android.app.ActivityManager,
   "download"         android.app.DownloadManager,
   "wifip2p"          android.net.wifi.p2p.WifiP2pManager,
   "user"             android.os.UserManager,
   "storage"          android.os.storage.StorageManager,
   "dropbox"          android.os.DropBoxManager,
   "nfc"              android.nfc.NfcManager,
   "accessibility"    android.view.accessibility.AccessibilityManager,
   "sensor"           android.hardware.SystemSensorManager,
   "power"            android.os.PowerManager,
   "connectivity"     android.net.ConnectivityManager,
   "location"         android.location.LocationManager,
   "usb"              android.hardware.usb.UsbManager,
   "media_router"     android.media.MediaRouter,
   "account"          android.accounts.AccountManager,
   "input"            android.hardware.input.InputManager,
   "layout_inflater"  com.android.internal.policy.impl.PhoneLayoutInflater,
   "audio"            android.media.AudioManager,
   "phone"            android.telephony.TelephonyManager,
   "device_policy"    android.app.admin.DevicePolicyManager,
   "wallpaper"        android.app.WallpaperManager,
   "display"          android.hardware.display.DisplayManager,
   "keyguard"         android.app.KeyguardManager,
   "textservices"     android.view.textservice.TextServicesManager,
   "notification"     android.app.NotificationManager,
   "search"           android.app.SearchManager,
   "netstats"         nil,
   "appwidget"        nil,
   "uimode"           android.app.UiModeManager,
   "bluetooth"        nil}
