package com.wellfactored.playbindings

import shapeless.{::, Generic, HList, HNil}

/**
  * Provide a generic function that can generate Shapeless Generic instances for
  * case classes with a single member, e.g. `Foo(s: String)` where the `Generic.Repr`
  * type is `String` rather than `String :: HNil`. From this we can build the various
  * binding types that Play uses, i.e. json `Reads` and `Writes` instances as well as
  * `PathBindable`s and `QueryStringBindable`s
  */
trait GenericValueWrapper {
  implicit def genGen[W, V, Repr <: HList](
                                            implicit
                                            gen: Generic.Aux[W, Repr],
                                            ev1: (V :: HNil) =:= Repr,
                                            ev2: Repr =:= (V :: HNil)
                                          ): Generic.Aux[W, V] = {
    new Generic[W] {
      override type Repr = V

      override def to(w: W) = gen.to(w).head

      override def from(v: V) = gen.from(v :: HNil)
    }
  }
}
