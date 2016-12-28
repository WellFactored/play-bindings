package com.wellfactored.playbindings

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
    * Wrap the value `v` in an instance of type 'W'
    */
  def wrap(v: V): W

  /**
    * Extract and return the value of type `V` from the instance of `W`
    */
  def unwrap(w: W): V
}
