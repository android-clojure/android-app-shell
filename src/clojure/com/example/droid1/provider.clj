(ns com.example.droid1.provider
  (:require [neko.notify :refer [toast]]))

;;; https://developer.android.com/reference/android/content/ContentProvider.html

;;; 見た中では以下が、SQLとも無関係で一番シンプルな実装だった
;;; http://qiita.com/wasnot/items/8329409a46f31f1724b3

(gen-class
 :name com.example.droid1.Provider
 :extends android.content.ContentProvider)

(def the-db
  "値を型変換するのが面倒なので、全部文字列で入れている"
  (atom {"taro" {:name "taro" :age "2" :blood-type "A"}
         "jiro" {:name "jiro" :age "12" :blood-type "O"}
         "goro" {:name "goro" :age "8" :blood-type "B"}}))

(defn- pick-up [rows cols]
  (let [cols (apply juxt (map keyword cols))]
    (->> rows
         (map (fn [row]
                (cols (get @the-db row)))))))
;; (pick-up ["taro" "goro"] ["name" "age"])
;; (pick-up ["jiro"] ["blood-type"])


(defn -onCreate []
  true)

;;; https://developer.android.com/guide/topics/providers/content-provider-basics.html#table2
;;; returns: a Cursor or null.
(defn -query [^android.net.Uri uri

              ;; SQL でいうと以下が select の 属性の並び
              ;; The list of columns to put into the cursor. If null
              ;; all columns are included.
              ^"[Ljava.lang.String;" projection

              ;; SQLだと以下2つが where 相当みたい
              ;; A selection criteria to apply when filtering
              ;; rows. If null then all rows are included.
              ^String selection
              ;; You may include ?s in selection, which will be
              ;; replaced by the values from selectionArgs, in order
              ;; that they appear in the selection. The values will be
              ;; bound as Strings.
              ^"[Ljava.lang.String;" selection-args

              ;; How the rows in the cursor should be sorted. If null
              ;; then the provider is free to define the sort order.
              ^String sort-order]
  (let [cursor (android.database.MatrixCursor. selection-args)]
    (->> (pick-up selection-args projection)
         (map #(.addRow cursor (object-array %)))
         dorun)
    cursor))


;;; returns: The URI for the newly inserted item.
;;; 個々のデータにURIが割当たる様にしないとダメなのか?
(defn -insert [^android.net.Uri uri
               ^android.content.ContentValues values]
  (android.net.Uri/parse "content://com.example.droid1.provider/"))

;;; returns: the number of rows affected
(defn -update [^android.net.Uri uri
               ^android.content.ContentValues values
               ^String selection
               selection-args         ;String[]
               ]
  0)

;;; returns: the number of rows affected
(defn -delete [^android.net.Uri uri
               ^String selection
               selection-args         ; String[]
               ]
  0)
