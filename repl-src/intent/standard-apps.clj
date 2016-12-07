(in-ns com.example.droid1.main)

(defmacro ^:private launch-app [app]
  `(.startActivity (*a)
                   (doto (android.content.Intent.)
                     (.setAction android.content.Intent/ACTION_MAIN)
                     (.addCategory (. android.content.Intent ~(symbol (str "CATEGORY_APP_" app))))
                     (.setFlags android.content.Intent/FLAG_ACTIVITY_NEW_TASK))))


(launch-app BROWSER)
(launch-app CALCULATOR)
(launch-app CALENDAR)
(launch-app CONTACTS)
(launch-app EMAIL)
(launch-app GALLERY)
(launch-app MAPS)
(launch-app MARKET)
(launch-app MESSAGING)
(launch-app MUSIC)
