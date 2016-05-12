package com.wellfactored.playbindings

import play.api.libs.json._

case class UserId(id: Long) extends AnyVal

object UserId extends ValidatingWrapper[Long, UserId] {
  override def validate(v: Long): Either[String, Long] = if (v > 0) Right(v) else Left("id must be greater than zero")
}

object Test {
  val i = UserId(5)

  val pvw = PlayBindings[Long, UserId]()

  import pvw._

  Json.toJson(i)

}

