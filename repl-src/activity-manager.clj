(in-ns com.example.droid1.main)
(require '[clojure.pprint :refer [pprint]])

;;; https://developer.android.com/reference/android/app/ActivityManager.html
;;; Activity だけだけど Unix の ps コマンドみたいな機能

(let [am (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
      mi (android.app.ActivityManager$MemoryInfo.)]
  (.getMemoryInfo am mi)
  (-> mi
      (inspect-fields android.app.ActivityManager$MemoryInfo)
      pprint))

;;; AndroidManifest に以下を追加する事
;;; <uses-permission android:name="android.permission.GET_TASKS" />
(-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
    (.getRunningTasks 10)
    (->> (map #(inspect-fields % android.app.ActivityManager$RunningTaskInfo)))
    pprint)

(let [flag 0]
 (-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
     (.getRecentTasks 10 flag)
     (->> (map #(inspect-fields % android.app.ActivityManager$RecentTaskInfo)))
     pprint))

(-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
    (.getRunningServices 10)
    (->> (map #(inspect-fields % android.app.ActivityManager$RunningServiceInfo)))
    pprint)

(-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
    .getRunningAppProcesses
    (->> (map #(inspect-fields % android.app.ActivityManager$RunningAppProcessInfo)))
    pprint)

(-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
    .getProcessesInErrorState
    (->> (map #(inspect-fields % android.app.ActivityManager$ProcessErrorStateInfo)))
    pprint)

;;; 使えなかった
(-> (.getSystemService (*a) android.content.Context/ACTIVITY_SERVICE)
    .getAppTasks
    (->> (map #(inspect-fields % android.app.ActivityManager$AppTask)))
    pprint)
