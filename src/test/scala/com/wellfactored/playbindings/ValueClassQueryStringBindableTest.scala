package com.wellfactored.playbindings

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}
import play.api.mvc.QueryStringBindable

class ValueClassQueryStringBindableTest extends FlatSpec with Matchers with OptionValues with EitherValues with ValueClassQueryStringBindable {

  case class LongWrapper(l: Long)

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[QueryStringBindable[LongWrapper]]

    b.bind("a", Map("a" -> Seq("1"))).value.right.value shouldBe LongWrapper(1)
  }

  it should "use a Validator if one is defined implicitly" in {
    implicit val vl = new Validator[LongWrapper, Long] {
      override def validate(l: Long): Either[String, Long] = if (l >= 0) Right(l) else Left(s"Id must be a non-negative integer ($l)")
    }

    val b = implicitly[QueryStringBindable[LongWrapper]]

    b.bind("a", Map("a" -> Seq("1"))).value.right.value shouldBe LongWrapper(1)
    b.bind("a", Map("a" -> Seq("-1"))).value.left.value shouldBe a[String]
  }

  case class StringWrapper(s: String)

  it should "use the normalised value returned from the validator" in {
    implicit val vl = new Validator[StringWrapper, String] {
      override def validate(s: String): Either[String, String] = Right(s.toLowerCase)
    }

    val b = implicitly[QueryStringBindable[StringWrapper]]

    b.bind("a", Map("a" -> Seq("UPPER"))).value.right.value shouldBe StringWrapper("upper")
  }


}
