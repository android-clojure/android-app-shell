(in-ns com.example.droid1.main)

(let [pm (.getPackageManager (*a))]
  (->> (.getSystemAvailableFeatures pm)
       (map #(inspect-fields % android.content.pm.FeatureInfo))
       print-table))


(let [pm (.getPackageManager (*a))]
  (->> (.getSystemAvailableFeatures pm)
       (keep #(:name (inspect-fields % android.content.pm.FeatureInfo)))
       (map (juxt identity #(.hasSystemFeature pm % #_version)))
       pprint))
