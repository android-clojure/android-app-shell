(in-ns com.example.droid1.main)

(let [a (doto (android.util.SparseArray.)
          ;; (.add R$id/x "abc")
          (.put 1 "a")
          (.put 2 "b")
          (.put 3 "c"))]
  [(.get a 1)
   (.get a 2)
   (.get a 3)])

(let [a (doto (android.util.SparseIntArray.)
          ;; (.add R$id/x "abc")
          (.put 1 (int 10))
          (.put 2 (int 20))
          (.put 3 (int 30)))]
  [(.get a 1)
   (.get a 2)
   (.get a 3)])

(let [a (doto (android.util.SparseLongArray.)
          ;; (.add R$id/x "abc")
          (.put 1 10)
          (.put 2 20)
          (.put 3 30))]
  [(.get a 1)
   (.get a 2)
   (.get a 3)])

(let [a (doto (android.util.LongSparseArray.)
          ;; (.add R$id/x "abc")
          (.put 1 10)
          (.put 2 20)
          (.put 3 30))]
  [(.get a 1)
   (.get a 2)
   (.get a 3)])

(let [a (doto (android.util.SparseBooleanArray.)
          ;; (.add R$id/x "abc")
          (.put 1 true)
          (.put 2 false)
          (.put 3 true))]
  [(.get a 1)
   (.get a 2)
   (.get a 3)])
