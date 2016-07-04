package com.wellfactored.playbindings

import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.mvc.PathBindable

class ValueClassPathBindableTest
  extends FlatSpec
    with Matchers
    with EitherValues
    with ValueClassPathBindable
    with TestValidators{

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[PathBindable[LongWrapper]]

    b.bind("", "1").right.value shouldBe LongWrapper(1)
  }

  it should "use a Validator if one is defined implicitly" in {
    implicit val vl = nonNegativeLong
    val b = implicitly[PathBindable[LongWrapper]]

    b.bind("", "1").right.value shouldBe LongWrapper(1)
    b.bind("", "-1").left.value shouldBe a[String]
  }

  it should "use the normalised value returned from the validator" in {
    implicit val vl = normaliseToLowerCase
    val b = implicitly[PathBindable[StringWrapper]]

    b.bind("", "UPPER").right.value shouldBe StringWrapper("upper")
  }

  "unbind" should "extract the wrapped value and convert it to a String" in {
    val b = implicitly[PathBindable[LongWrapper]]
    val lw = LongWrapper(1337)

    b.unbind("", lw) shouldBe "1337"
  }
}
