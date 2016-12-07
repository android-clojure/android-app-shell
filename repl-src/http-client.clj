(in-ns com.example.droid1.main)

;;; GET
(try
  (let [conn (-> (java.net.URL. "http://example.com")
                 .openConnection
                 (doto .connect))]
    (try
      (let [is (.getInputStream conn)
            sb (StringBuilder.)
            a  (byte-array 1024)]
        (loop [size (.read is a)]
          (when (< 0 size)
            (.append sb (String. a "utf-8"))))
        (str sb))
      (finally
        (.disconnect conn)))))

;;; POST
(try
  (let [conn (-> (java.net.URL. "http://httpbin.org/post")
                 .openConnection)]
    (try
      (doto conn
        (.setRequestMethod "POST")
        (.setDoOutput true))
      (doto (.getOutputStream conn)
        (.write (.getBytes "{\"name\":\"foo\",\"age\":5}"))
        .flush
        .close)
      (let [is (.getInputStream conn)
            sb (StringBuilder.)
            a  (byte-array 1024)]
        (loop [size (.read is a)]
          (when (< 0 size)
            (.append sb (String. a "utf-8"))))
        (str sb))
      (finally
        (.disconnect conn)))))
