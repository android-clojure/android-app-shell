(ns com.example.droid1.activity1
    (:require [neko.activity :refer [defactivity set-content-view!]]
              [neko.debug :refer [*a]]
              [neko.notify :refer [toast]]
              [neko.resource :as res]
              [neko.threading :refer [on-ui]]
              [com.example.droid1.inspector :refer :all]))

(res/import-all)
(def handlers (atom {}))

(defactivity com.example.droid1.Activity1
  :key :activity1

  (onCreate [this bundle]
            (.superOnCreate this bundle)
            (neko.debug/keep-screen-on this)
            ((or (:on-create @handlers)
                 (fn [saved-instance-state]
                   (set-content-view! (*a)
                                      (doto (android.widget.TextView. (*a))
                                        (.setBackgroundColor (android.graphics.Color/rgb 0x88 0 0x88))
                                        (.setText (str (.getClass (*a))))))))
             bundle))

  (onNewIntent [this intent]
               (.superOnNewIntent this intent)
               (when-let [f (:on-new-intent @handlers)]
                 (f intent))))
