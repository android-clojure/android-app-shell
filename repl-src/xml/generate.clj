(in-ns com.example.droid1.main)

(with-open [os (java.io.ByteArrayOutputStream.)
            w  (java.io.BufferedWriter. (java.io.OutputStreamWriter. os))]
  (let [xml (android.util.Xml/newSerializer)]
    ;; (.setOutput xml w)
    ;; (.flush xml)
    (doto xml
      (.setOutput w)
      (.startDocument nil Boolean/TRUE)
      (.setFeature "http://xmlpull.org/v1/doc/features.html#indent-output" true)
      (.startTag nil "root"))
    (doto xml
      (.endTag nil "root")
      .endDocument
      .flush))
  (.toString os))
