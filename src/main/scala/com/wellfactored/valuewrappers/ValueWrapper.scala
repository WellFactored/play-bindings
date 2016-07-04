package com.wellfactored.valuewrappers

import shapeless.{::, Generic, HNil, Lazy}

/**
  * `ValueWrapper` is a typeclass that abstracts the creation of a class that
  * wraps a single value of another type, as well as the action of extracting
  * the wrapped value from the wrapper. For example, for a single-member case class
  * the `wrap` method would use the companion object `apply` method to construct
  * an instance and the accessor for the member to extract the wrapped value.
  *
  * @tparam W the type of the wrapper
  * @tparam V the type of the wrapped value
  */
trait ValueWrapper[W, V] {
  /**
    * Attempt to wrap the value `v` in an instance of type 'W' and return it as a `Right`.
    * If `v` is somehow inappropriate to be wrapped then return an error message in
    * a `Left`
    */
  def wrap(v: V): Either[String, W]

  /**
    * Extract and return the value of type `V` from the instance of `W`
    */
  def unwrap(w: W): V
}

/**
  * This trait provides an implicit function that will generate a ValueWrapper[W,V]
  * for a case class of type `W` that has a single member of type `V`. This uses
  * Shapeless to summon a `Generic[W, V :: HNil]` to assist with the wrapping
  * and unwrapping, so it will actually work with any type `W` that is record-like
  * enough for Shapeless to handle.
  *
  * The `genWV` function also takes an implicit `Validator[W,V]` that allows for
  * some form of validation and manipulation of the value to be wrapped when
  * constructing the `W` instance.
  */
trait ValueWrapperGen {
  implicit def genWV[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                           vl: Validator[W, V]): ValueWrapper[W, V] =
    new ValueWrapper[W, V] {
      override def wrap(v: V): Either[String, W] =
        vl.validate(v).map(v2 => gen.value.from(v2 :: HNil)).toEither

      override def unwrap(w: W): V =
        gen.value.to(w).head
    }
}