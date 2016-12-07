(in-ns com.example.droid1.main)

;;; https://developer.android.com/reference/android/content/IntentFilter.html

;;; https://developer.android.com/guide/components/intents-filters.html#Resolution
;;; action: intent-filter で指定したもののいずれかが合致する事
;;; category: intent-filter で指定したものの全てが合致する事

;;; AndroidManifest.xml の形式で出力
(with-open [w (java.io.StringWriter.)]
  (let [xml (android.util.Xml/newSerializer)
        filter (doto (android.content.IntentFilter.)
                 (.addDataScheme "http")
                 (.addDataScheme "ftp")
                 (.addAction "aaa")
                 (.addCategory "ccc")
                 )]
    (.setOutput xml w)
    (.startTag xml nil "intent-filter")
    (.writeToXml filter xml)            ;readFromXml というのもあった
    (.endTag xml nil "intent-filter")
    (.flush xml))
  (.toString w))
;; => "<intent-filter><action name=\"aaa\" /><cat name=\"ccc\" /><scheme name=\"http\" /><scheme name=\"ftp\" /></intent-filter>"
;;; 属性の xmlns は抜けてた


(defn intent-filter [^android.content.IntentFilter filter]
  (cond-> {}
    (pos? (.countActions filter))         (conj [:actions          (map #(.getAction filter %) (range (.countActions filter)))])
    (pos? (.countCategories filter))      (conj [:categories       (map #(.getCategory filter %) (range (.countCategories filter)))])
    (pos? (.countDataAuthorities filter)) (conj [:data-authorities (map #(.getDataAuthorities filter %) (range (.countDataAuthorities filter)))])
    (pos? (.countDataPaths filter))       (conj [:data-paths       (map #(.getDataPaths filter %) (range (.countDataPaths filter)))])
    ;; (pos? (.countDataSchemeSpecificParts filter)) (conj [:data-scheme-specific-parts (map #(.getDataSchemeSpecificParts filter %) (range (.countDataSchemeSpecificParts filter)))])
    (pos? (.countDataSchemes filter))     (conj [:data-schemes     (map #(.getDataScheme filter %) (range (.countDataSchemes filter)))])
    (pos? (.countDataTypes filter))       (conj [:data-types       (map #(.getDataType filter %) (range (.countDataTypes filter)))])))

(let [;; 外側に intent-filter のタグはいらない
      ;; 属性に android: を付けるとエラーになった
      xml "<action name=\"aaa\" /><cat name=\"ccc\" /><scheme name=\"http\" /><scheme name=\"ftp\" />"
      filter (android.content.IntentFilter.)
      parser (-> (doto (android.util.Xml/newPullParser)
                   (.setInput (java.io.StringReader. xml))))]
  (.readFromXml filter parser)
  (intent-filter filter))
;; => {:actions ("aaa"), :categories ("ccc"), :data-schemes ("http" "ftp")}


;;; 方法が合っているか分からないけど、IntentFilter と Intent を 照合させてみる
;;; http://androidxref.com/7.0.0_r1/xref/frameworks/base/services/core/java/com/android/server/IntentResolver.java#688
;;; あたりとやっている事同じつもり
;;; あんまり内容分からんけど
(defn intent-filter-match-result [v]
  (or ((constant-mapper android.content.IntentFilter #"NO_MATCH_") v)
      {:category
       (-> v
           (bit-and android.content.IntentFilter/MATCH_CATEGORY_MASK)
           ((constant-mapper android.content.IntentFilter #"\AMATCH_CATEGORY_(?!MASK)")))

       :adjustment
       (-> v
           (bit-and android.content.IntentFilter/MATCH_ADJUSTMENT_MASK)
           ((constant-mapper android.content.IntentFilter #"\AMATCH_ADJUSTMENT_(?!MASK)")))}))


(let [filter (doto (android.content.IntentFilter.)
               (.addCategory android.content.Intent/CATEGORY_DEFAULT)
               (.addDataScheme "http"))
      intent (doto (android.content.Intent. android.content.Intent/ACTION_VIEW)
               (.setData (android.net.Uri/parse "http://example.com/")))]
  (intent-filter-match-result
   (.match filter
           (.getAction intent)
           (.getType intent)
           (.getScheme intent)
           (.getData intent)
           (.getCategories intent)
           "t")))
;; => :NO_MATCH_ACTION

(let [filter (android.content.IntentFilter.)
      intent (doto (android.content.Intent. android.content.Intent/ACTION_VIEW)
               (.setData (android.net.Uri/parse "http://example.com/")))]
  (intent-filter-match-result
   (.match filter
           (.getAction intent)
           (.getType intent)
           (.getScheme intent)
           (.getData intent)
           (.getCategories intent)
           "t")))
;; => :NO_MATCH_ACTION

(let [filter (doto (android.content.IntentFilter.)
               (.addAction android.content.Intent/ACTION_SEND)
               (.addAction android.content.Intent/ACTION_SEND_MULTIPLE)
               (.addCategory android.content.Intent/CATEGORY_DEFAULT)
               (.addDataType "image/*")
               (.addDataType "video/*"))
      intent (doto (android.content.Intent. android.content.Intent/ACTION_SEND)
               (.setType "image/jpeg")
               #_(.setData (android.net.Uri/parse "http://example.com/")))]
  (intent-filter-match-result
   (.match filter
           (.getAction intent)
           (.getType intent)
           (.getScheme intent)
           (.getData intent)
           (.getCategories intent)
           "t")))
;; => {:category :MATCH_CATEGORY_TYPE, :adjustment :MATCH_ADJUSTMENT_NORMAL}
