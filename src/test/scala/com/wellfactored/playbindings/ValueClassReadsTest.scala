package com.wellfactored.playbindings

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._

class ValueClassReadsTest extends FlatSpec with Matchers with ValueClassReads with TestValidators {
  "reads" should "implicitly summon a Reads for LongWrapper" in {
    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
  }
}
