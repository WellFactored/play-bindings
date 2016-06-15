package com.wellfactored.playbinders

import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.mvc.PathBindable

class GenericPathBindableTest extends FlatSpec with Matchers with EitherValues with GenericPathBindable {

  case class Test(l: Long)

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[PathBindable[Test]]

    b.bind("", "1").right.value shouldBe Test(1)
  }

  it should "use a Validator if one is defined implicitly" in {
    implicit val vl = new Validator[Test, Long] {
      override def validate(l: Long): Either[String, Long] = if (l >= 0) Right(l) else Left(s"Id must be a non-negative integer ($l)")
    }

    val b = implicitly[PathBindable[Test]]

    b.bind("", "1").right.value shouldBe Test(1)
    b.bind("", "-1").left.value shouldBe a[String]
  }


}
