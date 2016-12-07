(in-ns com.example.droid1.main)

(import com.example.droid1.R$anim)
(on-ui
 (set-content-view!
  (*a)
  (doto (android.widget.ImageView. (*a))
    (.setBackgroundColor (android.graphics.Color/rgb 0xaa 0x88 0xff))
    ;; (.setMinimumHeight 100)
    ;; (.setMinimumWidth 100)
    ;; (.setMaxHeight 100)
    ;; (.setMaxWidth 100)
    (.startAnimation (android.view.animation.AnimationUtils/loadAnimation (*a) R$anim/splash_rotation)))))
