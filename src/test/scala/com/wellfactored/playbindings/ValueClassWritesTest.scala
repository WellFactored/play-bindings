package com.wellfactored.playbindings

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.{JsNumber, Writes}

object testClasses {

  case class LongWrapper(l: Long) extends AnyVal

}

class ValueClassWritesTest extends FlatSpec with Matchers with ValueClassWrites {

  import testClasses.LongWrapper

  "writes" should "implicitly summon a Writes for LongWrapper" in {
    val w = implicitly[Writes[LongWrapper]]

    w.writes(LongWrapper(1337)) shouldBe JsNumber(1337)
  }

}
