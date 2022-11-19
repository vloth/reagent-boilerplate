(ns aux.test)

(defmacro let-wait
  [bindings & body]
  `(_wait-for (fn [] (promesa.core/let ~bindings ~@body))))
