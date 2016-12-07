(in-ns com.example.droid1.main)
(android.view.MenuItem)

(android.support.v7.widget.Toolbar. (*a))
(android.widget.Toolbar. (*a))

;;; 動かない
(on-ui
 (doto (.getActionBar (*a))
   (.setNavigationMode android.app.ActionBar/NAVIGATION_MODE_LIST)
   (.setListNavigationCallbacks
    (android.widget.ArrayAdapter. (*a)
                                  android.R$layout/simple_list_item_1
                                  ;; The id of the TextView within the layout resource to be populated
                                  android.R$id/text1 ;???
                                  (doto (java.util.ArrayList.)
                                    (.add "menu1")
                                    (.add "menu2")
                                    (.add "menu3")))
    (*a))
   (.setDisplayOptions 0 android.app.ActionBar/DISPLAY_SHOW_TITLE)))

;; (.getSupportActionBar (*a))
;; (.getActionBar (*a))
