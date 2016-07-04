package com.wellfactored.playbindings

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._

class ValueClassReadsTest extends FlatSpec with Matchers with ValueClassReads with TestValidators {
  "reads" should "implicitly summon a Reads for LongWrapper" in {
    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
  }

  it should "use a Validator if one is defined implicitly" in {
    implicit val vl = nonNegativeLong
    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
    r.reads(JsNumber(-1)) shouldBe a[JsError]
  }

  it should "use the normalised value returned from the validator" in {
    implicit val vl = normaliseToLowerCase
    val r = implicitly[Reads[StringWrapper]]

    r.reads(JsString("UPPER")) shouldBe JsSuccess(StringWrapper("upper"))
  }

}
