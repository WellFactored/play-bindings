package com.wellfactored.playbindings

import shapeless.{::, Generic, HNil, Lazy}

trait ValueWrapper[W, V] {
  def wrap(v: V): Either[String, W]

  def unwrap(w: W): V
}

trait ValueWrapperGen {
  implicit def genVW[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                           vl: Validator[W, V]): ValueWrapper[W, V] =
    new ValueWrapper[W, V] {
      override def wrap(v: V): Either[String, W] =
        vl.validate(v).right.map(v2 => gen.value.from(v2 :: HNil))

      override def unwrap(w: W): V =
        gen.value.to(w).head
    }
}