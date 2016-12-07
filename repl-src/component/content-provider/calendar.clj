(in-ns com.example.droid1.main)

;; //Froyo以前のOSの場合、content://calendar/calendarsを指定する

(let [^android.database.Cursor cursor
      (.managedQuery (*a)
                     (android.net.Uri/parse "content://com.android.calendar/calendars")
                     (into-array ["_id" "name"])
                     nil
                     nil
                     nil)]
  (.moveToFirst cursor)
  (let [idx-name (.getColumnIndex cursor "name")
        idx-id   (.getColumnIndex cursor "_id")]
    (loop [a []]
      (if (.moveToNext cursor)
        (let [name (.getString cursor idx-name)
              id   (.getString cursor idx-id)]
          ;; (prn name)
          ;; (prn id)
          (recur (conj a [id name])))
        a))))
