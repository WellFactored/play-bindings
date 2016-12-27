package com.wellfactored.playbindings

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}
import play.api.mvc.QueryStringBindable

class ValueClassQueryStringBindableTest
  extends FlatSpec
    with Matchers
    with OptionValues
    with EitherValues
    with ValueClassQueryStringBindable
    with TestValidators {

  val goodLongValue = Map("a" -> Seq("1"))
  val badLongValue = Map("a" -> Seq("-1"))

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[QueryStringBindable[LongWrapper]]

    b.bind("a", goodLongValue).value.right.value shouldBe LongWrapper(1)
  }

  "unbind" should "extract the wrapped value and convert it to a String" in {
    val b: QueryStringBindable[LongWrapper] = implicitly[QueryStringBindable[LongWrapper]]
    val lw = LongWrapper(1337)

    b.unbind("key", lw) shouldBe "key=1337"
  }


}
