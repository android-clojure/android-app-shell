(in-ns com.example.droid1.main)

(on-ui
 (-> (android.widget.Toast/makeText (*a)
                                    "foo"
                                    ;; android.widget.Toast/LENGTH_LONG
                                    android.widget.Toast/LENGTH_SHORT)
     .show))
