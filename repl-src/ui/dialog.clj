(in-ns com.example.droid1.main)
(require '[neko.threading :refer [on-ui]])

;;; 表示しなくても、コンストラクタの実行だけでエラーになるので注意
(android.app.ProgressDialog. (*a)) ;RuntimeException
(on-ui
 (android.app.ProgressDialog. (*a)))    ;OK


(on-ui
 (-> (android.app.AlertDialog$Builder. (*a))
     (.setTitle "foo")
     (.setMessage "bar")
     (.setPositiveButton "OK" nil)
     ;; .create
     .show))

(on-ui
 (-> (android.app.AlertDialog$Builder. (*a))
     (.setTitle "foo")
     (.setMessage "bar")
     (.setPositiveButton "OK"
                         (reify android.content.DialogInterface$OnClickListener
                           (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                            (toast (str "which:" which) :short))))

     .show))

(on-ui
 (-> (android.app.AlertDialog$Builder. (*a))
     (.setTitle "foo")
     (.setMessage "bar")
     (.setPositiveButton "OK"
                         (reify android.content.DialogInterface$OnClickListener
                           (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                            (toast "OK!!" :short))))
     (.setNegativeButton "Cancel"
                         (reify android.content.DialogInterface$OnClickListener
                           (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                            (toast "Cancel!!" :short))))
     (.setNeutralButton "xx"
                        (reify android.content.DialogInterface$OnClickListener
                          (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                           (toast "xx" :short))))
     .show))


(on-ui
 (-> (android.app.AlertDialog$Builder. (*a))
     (.setTitle "foo")
     (.setItems (into-array String ["a" "b" "c"])
                (reify android.content.DialogInterface$OnClickListener
                  (^void onClick [this ^android.content.DialogInterface dialog ^int idx]
                   (toast (str idx) :short))))
     .show))

(let [candidates ["a" "b" "c"]
      default-index 0                   ; = "a"
      selected (atom default-index)]
  (on-ui
   (-> (android.app.AlertDialog$Builder. (*a))
       (.setTitle "foo")
       (.setSingleChoiceItems (into-array String candidates)
                              default-index
                              (reify android.content.DialogInterface$OnClickListener
                                (^void onClick [this ^android.content.DialogInterface dialog ^int idx]
                                 (reset! selected idx))))
       (.setPositiveButton "OK"
                           (reify android.content.DialogInterface$OnClickListener
                             (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                              (toast (str (candidates @selected) " is selected!") :short))))
       .show)))

(require '[clojure.set :as set]
         '[clojure.string :as str])
(let [candidates ["a" "b" "c"]
      choosed (atom #{})]
  (on-ui
   (-> (android.app.AlertDialog$Builder. (*a))
       (.setTitle "foo")
       (.setMultiChoiceItems (into-array String candidates)
                             nil
                             (reify android.content.DialogInterface$OnMultiChoiceClickListener
                               (^void onClick [this ^android.content.DialogInterface dialog
                                               ^int idx
                                               ^boolean checked?]
                                (if checked?
                                  (swap! choosed conj idx)
                                  (swap! choosed set/difference #{idx})))))
       (.setPositiveButton "OK"
                           (reify android.content.DialogInterface$OnClickListener
                             (^void onClick [this ^android.content.DialogInterface dialog ^int which]
                              (toast (str (->> @choosed
                                               sort
                                               (map candidates)
                                               (str/join ", "))
                                          " are selected!") :short))))
       .show)))

;; 動かない
;; android.app.AlertDialog の作成も ui thread でやらないとダメなのかも
#_(let [adp (doto (android.widget.ArrayAdapter. (*a) android.R$layout/simple_list_item_1)
              (.add "item"))
        dialog (-> (android.app.AlertDialog$Builder. (*a))
                   (.setTitle "foo")
                   (.setMessage "bar")
                   (.setAdapter adp nil)
                   .create)]
    (on-ui (.show dialog)))

(let [adp (doto (android.widget.ArrayAdapter. (*a) android.R$layout/simple_list_item_1)
            (.add "0"))]
  (on-ui (-> (android.app.AlertDialog$Builder. (*a))
             (.setTitle "foo")
             (.setAdapter adp nil)
             .create
             .show))
  (.add adp "1")
  (.add adp "2")
  (.add adp "3"))

;;; https://developer.android.com/reference/android/app/ProgressDialog.html
(on-ui
 (doto (android.app.ProgressDialog. (*a))
   (.setProgressStyle android.app.ProgressDialog/STYLE_SPINNER)
   (.setMessage "Loading...")
   (.setCancelable true)
   .show))

;;; HandlerThread とか Looper の扱いが正しいかよく分からない
;;; 実はずっと待ちの状態になっていて、GCされてないとかなっていないのか?
;;; あと、キャンセルした後に .setProgress するのはどうなんだ?
(on-ui
 (let [max            200
       dialog         (doto (android.app.ProgressDialog. (*a))
                        (.setProgressStyle android.app.ProgressDialog/STYLE_HORIZONTAL)
                        (.setMessage "Loading...")
                        (.setMax max)
                        (.setCancelable true))
       handler-thread (android.os.HandlerThread. "foo")]
   (.show dialog)
   (.start handler-thread)
   (.post (-> handler-thread .getLooper android.os.Handler.)
          (fn []
            (loop [x 0]
              (when (<= x max)
                (.setProgress dialog (int x))
                (Thread/sleep 200)
                (recur (+ 10 x))))
            (.quit handler-thread)))))

;;; UIスレッドで全部処理しようとすると、経過中の状態も分からない
;;; という事は、UIスレッドに post された処理を全部処理した段階で
;;; まとめて描画とかしているのかな?
;;;
;;; 途中でキャンセルは出来ない気がしたけど、それよりもヒドい状態になった
(on-ui
 (let [max    200
       dialog (doto (android.app.ProgressDialog. (*a))
                (.setProgressStyle android.app.ProgressDialog/STYLE_HORIZONTAL)
                (.setMessage "Loading...")
                (.setMax max)
                (.setCancelable true))]
   (.show dialog)
   (loop [x 0]
     (when (<= x max)
       (.setProgress dialog (int x))
       (Thread/sleep 200)
       (recur (+ 10 x))))))
