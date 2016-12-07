(in-ns com.example.droid1.main)

;; |                           :name |                          :return-type |   :parameter-types |
;; |---------------------------------+---------------------------------------+--------------------|
;; |                    getActionBar |                 android.app.ActionBar |                 [] |
;; |                  getApplication |               android.app.Application |                 [] |
;; |              getCallingActivity |         android.content.ComponentName |                 [] |
;; |               getCallingPackage |                      java.lang.String |                 [] |
;; |       getChangingConfigurations |                                   int |                 [] |
;; |                getComponentName |         android.content.ComponentName |                 [] |
;; |                 getContentScene |              android.transition.Scene |                 [] |
;; |     getContentTransitionManager |  android.transition.TransitionManager |                 [] |
;; |                 getCurrentFocus |                     android.view.View |                 [] |
;; |              getFragmentManager |           android.app.FragmentManager |                 [] |
;; |                       getIntent |                android.content.Intent |                 [] |
;; | getLastNonConfigurationInstance |                      java.lang.Object |                 [] |
;; |               getLayoutInflater |           android.view.LayoutInflater |                 [] |
;; |                getLoaderManager |             android.app.LoaderManager |                 [] |
;; |               getLocalClassName |                      java.lang.String |                 [] |
;; |              getMediaController | android.media.session.MediaController |                 [] |
;; |                 getMenuInflater |             android.view.MenuInflater |                 [] |
;; |                       getParent |                  android.app.Activity |                 [] |
;; |         getParentActivityIntent |                android.content.Intent |                 [] |
;; |                  getPreferences |     android.content.SharedPreferences |              [int] |
;; |                     getReferrer |                       android.net.Uri |                 [] |
;; |         getRequestedOrientation |                                   int |                 [] |
;; |                  getSearchEvent |              android.view.SearchEvent |                 [] |
;; |                getSystemService |                      java.lang.Object | [java.lang.String] |
;; |                       getTaskId |                                   int |                 [] |
;; |                        getTitle |                java.lang.CharSequence |                 [] |
;; |                   getTitleColor |                                   int |                 [] |
;; |              getVoiceInteractor |           android.app.VoiceInteractor |                 [] |
;; |          getVolumeControlStream |                                   int |                 [] |
;; |                       getWindow |                   android.view.Window |                 [] |
;; |                getWindowManager |            android.view.WindowManager |                 [] |

(= (.getLayoutInflater (*a))
   (.getSystemService (*a) android.content.Context/LAYOUT_INFLATER_SERVICE)
   (android.view.LayoutInflater/from (*a)))
;; => true


;;; R$id/splash_app_name がグローバルな名前なのがキモい
;;; なんで nil なのか?
(.findViewById (*a) R$id/splash_app_name) ;; => nil


(.getActionBar (*a))
;; => #object[com.android.internal.app.ActionBarImpl 0x9ee71918 "com.android.internal.app.ActionBarImpl@9ee71918"]

(.getApplication (*a))
;; => #object[neko.App 0x9e74f040 "neko.App@9e74f040"]

(.getCallingActivity (*a))
;; => nil

(.getCallingPackage (*a))
;; => nil

;;; 多分↓のマスク値
;;; https://developer.android.com/reference/android/R.attr.html#configChanges
(.getChangingConfigurations (*a))
;; => 0

(.getComponentName (*a))
;; => #object[android.content.ComponentName 0x9ea87e50 "ComponentInfo{com.example.droid1/com.example.droid1.MainActivity}"]

(.getCurrentFocus (*a))
;; => #object[android.widget.NumberPicker$CustomEditText 0x9ef48e10 "android.widget.NumberPicker$CustomEditText{9ef48e10 VFED..CL .F...... 0,155-128,204 #1020336 android:id/numberpicker_input}"]

(.getFragmentManager (*a))
;; => #object[android.app.FragmentManagerImpl 0x9ee16ed8 "FragmentManager{9ee16ed8 in MainActivity{9ef43d98}}"]

(.getIntent (*a))
;; => #object[android.content.Intent 0x9edfa728 "Intent { act=com.example.droid1.MAIN cmp=com.example.droid1/.MainActivity }"]

(.getLastNonConfigurationInstance (*a))
;; => nil

(.getLayoutInflater (*a))
;; => #object[com.android.internal.policy.impl.PhoneLayoutInflater 0x9eddb7e0 "com.android.internal.policy.impl.PhoneLayoutInflater@9eddb7e0"]

(.getLoaderManager (*a))
;; => #object[android.app.LoaderManagerImpl 0x9f2fbbb8 "LoaderManager{9f2fbbb8 in MainActivity{9ef43d98}}"]

(.getLocalClassName (*a))
;; => "MainActivity"

(.getMenuInflater (*a))
;; => #object[android.view.MenuInflater 0x9ed958e0 "android.view.MenuInflater@9ed958e0"]

(.getParent (*a))
;; => nil

(.getParentActivityIntent (*a))
;; => nil

(.getRequestedOrientation (*a))
;; => -1

(.getTaskId (*a))
;; => 3

(.getTitle (*a))
;; => "droid1"

(.getTitleColor (*a))
;; => 0

;; (.getVoiceInteractor (*a))
(format "%x"(.getVolumeControlStream (*a)))
;; => "80000000"

(.getWindow (*a))
;; => #object[com.android.internal.policy.impl.PhoneWindow 0x9e93f7c0 "com.android.internal.policy.impl.PhoneWindow@9e93f7c0"]

(.getWindowManager (*a))
;; => #object[android.view.WindowManagerImpl 0x9edd29a0 "android.view.WindowManagerImpl@9edd29a0"]

;;; 失敗した
;; (.getContentScene (*a))
;; (.getContentTransitionManager (*a))
;; (.getMediaController (*a))
;; (.getReferrer (*a))
;; (.getSearchEvent (*a))
