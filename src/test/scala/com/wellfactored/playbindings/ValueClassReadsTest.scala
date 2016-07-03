package com.wellfactored.playbindings

import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.libs.json._

class ValueClassReadsTest extends FlatSpec with Matchers  with ValueClassReads {

  case class LongWrapper(l: Long)

  "reads" should "implicitly summon a Reads for LongWrapper" in {
    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
  }

  it should "use a Validator if one is defined implicitly" in {
    implicit val vl = new Validator[LongWrapper, Long] {
      override def validate(l: Long): Either[String, Long] = if (l >= 0) Right(l) else Left(s"Id must be a non-negative integer ($l)")
    }

    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
    r.reads(JsNumber(-1)) shouldBe a[JsError]
  }

  case class StringWrapper(s: String)

  it should "use the normalised value returned from the validator" in {
    implicit val vl = new Validator[StringWrapper, String] {
      override def validate(s: String): Either[String, String] = Right(s.toLowerCase)
    }

    val r = implicitly[Reads[StringWrapper]]

    r.reads(JsString("UPPER")) shouldBe JsSuccess(StringWrapper("upper"))
  }

}
