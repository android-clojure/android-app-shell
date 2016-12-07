(in-ns com.example.droid1.main)

(defn device-configuration [^android.content.res.Configuration conf]
  (-> conf
      (inspect-fields android.content.res.Configuration)
      ;; ((field-inspector android.content.res.Configuration))

      (update :uiMode
              (fn [uiMode]
                {:type  (-> uiMode
                            (bit-and android.content.res.Configuration/UI_MODE_TYPE_MASK)
                            ((constant-mapper android.content.res.Configuration #"\AUI_MODE_TYPE_(?!MASK)")))
                 :night (-> uiMode
                            (bit-and android.content.res.Configuration/UI_MODE_NIGHT_MASK)
                            ((constant-mapper android.content.res.Configuration #"\AUI_MODE_NIGHT_(?!MASK)")))}))

      (update :orientation
              (constant-mapper android.content.res.Configuration #"\AORIENTATION_"))

      (update :keyboard
              (constant-mapper android.content.res.Configuration #"\AKEYBOARD_"))
      (update :keyboardHidden
              (constant-mapper android.content.res.Configuration #"\AKEYBOARDHIDDEN_"))
      (update :hardKeyboardHidden
              (constant-mapper android.content.res.Configuration #"\AHARDKEYBOARDHIDDEN_"))

      (update :navigation
              (constant-mapper android.content.res.Configuration #"\ANAVIGATION_"))
      (update :navigationHidden
              (constant-mapper android.content.res.Configuration #"\ANAVIGATIONHIDDEN_"))

      (update :touchscreen
              (constant-mapper android.content.res.Configuration #"\ATOUCHSCREEN_"))
      (update :screenLayout
              (fn [screenLayout]
                (if (= screenLayout android.content.res.Configuration/SCREENLAYOUT_UNDEFINED)
                  :SCREENLAYOUT_UNDEFINED
                  {:direction (-> screenLayout
                                  (bit-and android.content.res.Configuration/SCREENLAYOUT_LAYOUTDIR_MASK)
                                  ((constant-mapper android.content.res.Configuration #"\ASCREENLAYOUT_LAYOUTDIR_(?!MASK)")))
                   :long      (-> screenLayout
                                  (bit-and android.content.res.Configuration/SCREENLAYOUT_LONG_MASK)
                                  ((constant-mapper android.content.res.Configuration #"\ASCREENLAYOUT_LONG_(?!MASK)")))
                   :size      (-> screenLayout
                                  (bit-and android.content.res.Configuration/SCREENLAYOUT_SIZE_MASK)
                                  ((constant-mapper android.content.res.Configuration #"\ASCREENLAYOUT_SIZE_(?!MASK)")))
                   #_:round   #_(-> screenLayout
                                    (bit-and android.content.res.Configuration/SCREENLAYOUT_ROUND_MASK)
                                    ((constant-mapper android.content.res.Configuration #"\ASCREENLAYOUT_ROUND_(?!MASK)")))})))

      #_(update :mnc
                #(if (= % android.content.res.Configuration/MNC_ZERO)
                   :MNC_ZERO
                   %))
      (update :screenHeightDp
              #(if (= % android.content.res.Configuration/SCREEN_HEIGHT_DP_UNDEFINED)
                 :SCREEN_HEIGHT_DP_UNDEFINED
                 %))
      (update :screenWidthDp
              #(if (= % android.content.res.Configuration/SCREEN_WIDTH_DP_UNDEFINED)
                 :SCREEN_WIDTH_DP_UNDEFINED
                 %))
      (update :smallestScreenWidthDp
              #(if (= % android.content.res.Configuration/SMALLEST_SCREEN_WIDTH_DP_UNDEFINED)
                 :SMALLEST_SCREEN_WIDTH_DP_UNDEFINED
                 %))
      (update :densityDpi
              #(if (= % android.content.res.Configuration/DENSITY_DPI_UNDEFINED)
                 :DENSITY_DPI_UNDEFINED
                 %))))

(-> (*a) .getResources .getConfiguration
    device-configuration
    pprint)

;;; 画面に関しては、ここからも情報が取れるっぽい
(-> (*a)
    .getWindowManager
    .getDefaultDisplay
    (inspect-getters android.view.Display)
    (update :Flags #(keywordize-bitset % android.view.Display #"\AFLAG_"))
    pprint)
