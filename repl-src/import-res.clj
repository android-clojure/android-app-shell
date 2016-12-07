(in-ns neko.resource)

(defmacro import-all
  "Imports all existing application's R subclasses (R$drawable, R$string etc.)
  into the current namespace."
  []
  `(do ~@(map (fn [res-type]
                `(try (import '~(-> (app-package-name)
                                    (str ".R$" res-type)
                                    symbol))
                      (catch ClassNotFoundException _# nil)))
              '[anim drawable color layout menu string array plurals style id
                xml attr styleable
                bool
                dimen])))

(import-all)
