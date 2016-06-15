package com.wellfactored.playbinders

/**
  * Defines a validation of values of type `V` in the context of another type `W`, for example
  * if `W` instance wrap values of type `V`
  *
  * So if you have `case class UserId(id: Long)` you might define an implicit `Validator[UserId, Long]`
  * that checks that the `Long` value is non-negative.
  *
  * @tparam W The context in which the validation takes place
  * @tparam V The type of the values that will be validated
  */
trait Validator[W, V] {
  def validate(a: V): Either[String, V]
}

trait ValidatorLowPriorityImplicits {
  /**
    * Define a low-priority instance that allows any value of type `V`. When using the PlayBinders
    * this instance will get picked up unless you define your own implicit instance to override it.
    */
  implicit def validator[W, V]: Validator[W, V] = new Validator[W, V] {
    override def validate(a: V): Either[String, V] = Right(a)
  }
}

/**
  * Pull the low-priority instances into the companion object so they'll be available in
  * the implicit scope.
  */
object Validator extends ValidatorLowPriorityImplicits
