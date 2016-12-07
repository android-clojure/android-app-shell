(in-ns com.example.droid1.main)


(android.graphics.Color/rgb 10 20 30)
;; => -16116706

(android.graphics.Color/parseColor "#102030")
;; => -15720400

(android.graphics.Color/parseColor "red")
;; => -65536

(let [c (android.graphics.Color/rgb 10 20 30)]
  [(android.graphics.Color/red c)
   (android.graphics.Color/blue c)
   (android.graphics.Color/green c)])
;; => [10 30 20]
