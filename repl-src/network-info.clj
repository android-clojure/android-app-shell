(in-ns com.example.droid1.main)

(-> (.getSystemService (*a) android.content.Context/CONNECTIVITY_SERVICE)
    .getActiveNetworkInfo
    (inspect-getters android.net.NetworkInfo)
    (update :Type (constant-mapper android.net.ConnectivityManager #"\ATYPE_"))
    pprint)
;;; 中の android.net.NetworkInfo$DetailedState, android.net.NetworkInfo$State
;;; は enum なので、さらに細かく見る必用はない

(-> (.getSystemService (*a) android.content.Context/CONNECTIVITY_SERVICE)
    .getActiveNetworkInfo
    .describeContents
    (bit-and android.os.Parcelable/CONTENTS_FILE_DESCRIPTOR))
