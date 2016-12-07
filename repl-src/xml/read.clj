(in-ns com.example.droid1.main)

(def ^:private xml-event
  (constant-mapper org.xmlpull.v1.XmlPullParser
                   [START_DOCUMENT END_DOCUMENT START_TAG END_TAG]))

(let [x (-> (*a)
            .getResources
            (.getXml com.example.droid1.R$xml/month))]
  [(xml-event (.getEventType x))
   (do (.next x)
       (xml-event (.getEventType x)))
   (do (.next x)
       (xml-event (.getEventType x)))
   (do (.next x)
       (xml-event (.getEventType x)))])


(let [xml "<?xml version=\"1.0\" encoding=\"utf-8\"?>
<root>
  <item>
    <title>January</title>
    <number>1</number>
  </item>
  <item>
    <title>February</title>
    <number>2</number>
  </item>
  <item>
    <title>March</title>
    <number>3</number>
  </item>
</root>"
      x  (doto (android.util.Xml/newPullParser)
           (.setInput (java.io.StringReader. xml)))]
  [(xml-event (.getEventType x))
   (do (.next x)
       (xml-event (.getEventType x)))
   (do (.next x)
       (xml-event (.getEventType x)))
   (do (.next x)
       (xml-event (.getEventType x)))])
