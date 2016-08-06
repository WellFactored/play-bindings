package com.wellfactored.playbindings

import cats.data.Validated
import cats.syntax.validated._
import com.wellfactored.valuewrapper.Validator

trait TestValidators {

  case class StringWrapper(s: String)

  val normaliseToLowerCase = new Validator[StringWrapper, String] {
    override def validate(s: String): Validated[String, String] = s.toLowerCase.valid
  }

  case class LongWrapper(l: Long)

  val nonNegativeLong = new Validator[LongWrapper, Long] {
    override def validate(l: Long): Validated[String, Long] =
      if (l >= 0) l.valid else s"Id must be a non-negative integer ($l)".invalid
  }
}
