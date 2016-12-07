(in-ns com.example.droid1.main)

;;; 色んな分類方法がある
;;; - 見るだけ or 入力もする
;;; - 用途の限定度 DatePicker はかなり限定されている、EditText はそうでもない
;;; - ↑を考えると色んなものがグラデーションを持って存在しているので、分類がやり難いと思う
;;; - そもそも素人が分類しようとするのが無謀な気がする

(require '[neko.ui :refer [make-ui]]
         '[neko.notify :refer [fire notification]]
         '[neko.ui.adapters :refer [ref-adapter]])

(def widget (atom nil))


;; TextView
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.TextView. (*a))
                      #_(.onTouchEvent )
                      #_.setSingleLine
                      #_(.setTextSize 0.5)
                      ;; (.setBackgroundColor android.graphics.Color/BLUE)
                      (.setBackgroundColor (android.graphics.Color/rgb 0xaa 0x88 0xff))
                      (.setTextColor (android.graphics.Color/rgb 0x66 0xff 0x66))
                      (.setText "foo"))))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.TextView. (*a))
                      #_(.onTouchEvent )
                      #_.setSingleLine
                      #_(.setTextSize 0.5)
                      (.setText R$string/input_is_empty))))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.TextView. (*a))
                      (.setBackground (-> (*a)
                                          .getResources
                                          (.getDrawable R$drawable/splash_circle)))
                      (.setText R$string/input_is_empty))))


;; ProgressBar
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.ProgressBar. (*a))
                      (.setBackgroundColor android.graphics.Color/LTGRAY))))
(on-ui
 (set-content-view!
  (*a)
  (doto (android.widget.ProgressBar. (*a) nil
                                     android.R$attr/progressBarStyleInverse
                                     ;; どれを指定してもあまり違いはない
                                     ;; android.R$attr/progressBarStyleLarge
                                     ;; android.R$attr/progressBarStyleLargeInverse
                                     ;; android.R$attr/progressBarStyleSmall
                                     ;; android.R$attr/progressBarStyleSmallInverse
                                     ;; android.R$attr/progressBarStyleSmallTitle
                                     ))))

(on-ui
 (let [max            200
       progress       (doto (android.widget.ProgressBar. (*a) nil
                                                         android.R$attr/progressBarStyleHorizontal)
                        (.setMax max))
       handler-thread (android.os.HandlerThread. "foo")]
   (set-content-view! (*a) progress)
   (.start handler-thread)
   (.post (-> handler-thread .getLooper android.os.Handler.)
          (fn []
            (loop [x 0]
              (when (<= x max)
                (.setProgress progress (int x))
                (Thread/sleep 200)
                (recur (+ 10 x))))
            (.quit handler-thread)))))


;;; ImageView
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.ImageView. (*a))
                      (.setImageResource R$drawable/ic_launcher))))

;;; 時間がかかる画像のダウンロードを非UIスレッドで実行しているけど、
;;; この方法は、REPLが非UIスレッドで動いている事を前提にしているので
;;; そのまま Java に移植できない
(let [conn (-> (java.net.URL. "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Giant_panda01_960.jpg/320px-Giant_panda01_960.jpg")
               .openConnection       ;本当はこれの例外も拾う必用がある
               (->> (cast java.net.HttpURLConnection)))

      ^android.graphics.Bitmap bmp
      (try
        (doto conn
          (.setRequestMethod "GET")
          .connect)
        (with-open [is (.getInputStream conn)]
          (android.graphics.BitmapFactory/decodeStream is))
        (catch Exception e)
        (finally
          (.disconnect conn)))]
  (on-ui
   (set-content-view! (*a)
                      (doto (android.widget.ImageView. (*a))
                        (.setImageBitmap bmp)))))

;;; これだとなぜか上手くいかない
(let [image-view (android.widget.ImageView. (*a))]
  (on-ui
   (set-content-view! (*a) image-view)
   ;; ↓の .post は UIスレッドの中でも外でも上手くいかなかった
   (.post image-view
          (fn []
            (prn "foo")        ;表示されてないので、実行されてないかも
            (let [conn (-> (java.net.URL. "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Giant_panda01_960.jpg/320px-Giant_panda01_960.jpg")
                           .openConnection ;本当はこれの例外も拾う必用がある
                           (->> (cast java.net.HttpURLConnection)))

                  ^android.graphics.Bitmap bmp
                  (try
                    (doto conn
                      (.setRequestMethod "GET")
                      .connect)
                    (with-open [is (.getInputStream conn)]
                      (android.graphics.BitmapFactory/decodeStream is))
                    (catch Exception e)
                    (finally
                      (.disconnect conn)))]
              (.setImageBitmap image-view bmp))))))

(let [image-view (android.widget.ImageView. (*a))

      ;; https://developer.android.com/reference/android/os/AsyncTask.html
      ;; Java だとジェネリクスで型パラメタを指定する必用がある
      task       (proxy [android.os.AsyncTask] []
                   (doInBackground [urls]
                     (let [conn (-> urls
                                    first
                                    java.net.URL.
                                    .openConnection
                                    (->> (cast java.net.HttpURLConnection)))

                           ^android.graphics.Bitmap bmp
                           (try
                             (doto conn
                               (.setRequestMethod "GET")
                               .connect)
                             (with-open [is (.getInputStream conn)]
                               (android.graphics.BitmapFactory/decodeStream is))
                             (catch Exception e)
                             (finally
                               (.disconnect conn)))]
                       bmp))
                   (onPostExecute [bmp]
                     (.setImageBitmap image-view bmp)))]
  (on-ui
   (set-content-view! (*a) image-view)
   ;; ↓の .execute も UIスレッドで実行しないと上手くいかなかった
   (.execute task (object-array ["https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Giant_panda01_960.jpg/320px-Giant_panda01_960.jpg"]))))

;; ImageView imageView = (ImageView)findViewById(R.id.imageView);
;; (android.net.Uri/parse "http://cdn-ak.f.st-hatena.com/images/fotolife/f/fjswkun/20150927/20150927140905.jpg")
;; Uri.Builder builder = uri.buildUpon()
;; AsyncTaskHttpRequest task = new AsyncTaskHttpRequest(imageView)
;; task.execute(builder)

#_(android.provider.MediaStore$Images$Media/getBitmap
 (.getContentResolver (*a))
 (android.net.Uri/parse path))


#_(on-ui
   (set-content-view! (*a)
                      (doto (android.widget.KyeboardView. (*a  xxx))
                        )))


;; あまり意味はないかも
(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.Space. (*a))
                      )))

;; ???
(on-ui
 (set-content-view! (*a)
                    (doto (android.view.SurfaceView. (*a))
                      )))

;;
(on-ui
 (set-content-view! (*a)
                    (doto (android.view.TextureView. (*a))
                      ;; (.setBackgroundDrawable )
                      ;; (.setForeground )
                      )))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.RatingBar. (*a))
                      (.setMax 50)
                      (.setNumStars 10)
                      (.setRating 5)
                      ;; (.setOnRatingBarChangeListener)
                      )))

(on-ui
 (set-content-view! (*a)
                    (doto (android.widget.SeekBar. (*a))
                      )))







;;;
;;; java.lang.ClassNotFoundException
(android.support.v7.widget.RecyclerView. (*a))






;; AdapterViewAnimator
;; AdapterViewFlipper
;; AlphabetIndexer
;; CheckBox
;; Chronometer

;; DialerFilter

;; EditText
;; Filter
;; Filter.FilterResults
;; ImageSwitcher

;; ListPopupWindow
;; PopupMenu
;; PopupWindow

;; OverScroller
;; Scroller

;; MediaController
;; ProgressBar
;; QuickContactBadge
;; RatingBar
;; RemoteViews
;; RemoteViewsService

;; SeekBar
;; ShareActionProvider


;; SlidingDrawer
;; Space
;; Spinner
;; Switch
;; TabHost
;; TabHost.TabSpec
;; TabWidget
;; TextSwitcher
;; TwoLineListItem
;; ViewAnimator
;; ViewFlipper
;; ViewSwitcher
;; ZoomControls


;; ActionMenuView
;; ActionMenuView.LayoutParams
;; AdapterView
;; AdapterView.AdapterContextMenuInfo
;; AutoCompleteTextView
;; CalendarView
;; CheckedTextView
;; ExpandableListView
;; ExpandableListView.ExpandableListContextMenuInfo
;; GridView
;; HorizontalScrollView
;; ImageView
;; ListView
;; ListView.FixedViewInfo
;; MultiAutoCompleteTextView
;; MultiAutoCompleteTextView.CommaTokenizer
;; ScrollView
;; SearchView
;; StackView
;; TextView
;; TextView.SavedState
;; VideoView



;;; 形がない?
;; EdgeEffect

;;; Abs は作れない
;; AbsListView
;; AbsListView.LayoutParams
;; AbsSeekBar
;; AbsSpinner

;;; 他と一緒じゃないと意味がない
;; BaseAdapter
;; BaseExpandableListAdapter
;; ArrayAdapter
;; CursorAdapter
;; CursorTreeAdapter
;; HeaderViewListAdapter
;; ResourceCursorAdapter
;; ResourceCursorTreeAdapter
;; SimpleAdapter
;; SimpleCursorAdapter
;; SimpleCursorTreeAdapter
;; SimpleExpandableListAdapter
