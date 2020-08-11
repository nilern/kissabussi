(ns kissabussi.core
  (:require [cats.protocols :as cp]
            [taksi.core :as taksi #?@(:cljs [:refer [Task]])])
  #?(:clj (:import [taksi.core Task])))

(def context
  (reify
    cp/Context

    cp/Functor
    (-fmap [_ f fv] (taksi/transform f fv))

    cp/Applicative
    (-pure [_ v] (taksi/resolved v))
    (-fapply [_ af av] (taksi/then af (fn [f] (taksi/transform f av))))

    cp/Monad
    (-mreturn [_ v] (taksi/resolved v))
    (-mbind [_ mv f] (taksi/then mv f))))

(extend-type Task
  cp/Contextual
  (-get-context [_] context))
