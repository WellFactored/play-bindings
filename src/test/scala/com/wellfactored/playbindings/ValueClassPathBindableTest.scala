package com.wellfactored.playbindings

import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.mvc.PathBindable

class ValueClassPathBindableTest
  extends FlatSpec
    with Matchers
    with EitherValues
    with ValueClassPathBindable
    with TestValidators {

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[PathBindable[LongWrapper]]

    b.bind("", "1").right.value shouldBe LongWrapper(1)
  }


  "unbind" should "extract the wrapped value and convert it to a String" in {
    val b = implicitly[PathBindable[LongWrapper]]
    val lw = LongWrapper(1337)

    b.unbind("", lw) shouldBe "1337"
  }
}
