(in-ns com.example.droid1.main)

(.startService (*a) (android.content.Intent. (*a) com.example.droid1.Service))
(.stopService (*a) (android.content.Intent. (*a) com.example.droid1.Service))
