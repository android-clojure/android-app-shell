(in-ns com.example.droid1.main)

;;; Cursor
(let [c (doto (android.database.MatrixCursor. (into-array String ["key" "value"]))
          (.addRow (object-array ["a" (Integer. 1)]))
          (.addRow (object-array ["b" (Integer. 2)]))
          (.addRow (object-array ["c" (Integer. 3)])))

      cs (.getColumnNames c)]
  (.moveToFirst c)
  (repeatedly (.getCount c)
              #(let [x (zipmap cs
                               [(.getString c 0) (.getInt c 1)])]
                 (.moveToNext c)
                 x)))
;; => ({"key" "a", "value" 1} {"key" "b", "value" 2} {"key" "c", "value" 3})

(let [c (doto (android.database.MatrixCursor. (into-array String ["key" "value"]))
          (.addRow (object-array ["a" (Integer. 1)]))
          (.addRow (object-array ["b" (Integer. 2)]))
          (.addRow (object-array ["c" (Integer. 3)])))

      cs (.getColumnNames c)]
  (.moveToFirst c)
  (loop [xs []]
    (if (.isLast c)
      xs                ;isLastの段階であともう1回データが取れるっぽい
      (let [x (zipmap cs
                      [(.getString c 0) (.getInt c 1)])]
        (.moveToNext c)
        (recur (conj xs x))))))
;; => [{"key" "a", "value" 1} {"key" "b", "value" 2}]


;;;
(let [vs (doto (android.content.ContentValues.)
           (.put "a" 1)
           (.put "b" 2)
           (.put "c" 3))]
  [(.getAsInteger vs "a")
   (.getAsInteger vs "b")
   (.getAsInteger vs "c")])
;; => [1 2 3]
