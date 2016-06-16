package com.wellfactored.playbindings

import play.api.libs.json._
import shapeless.{::, Generic, HNil, Lazy}

trait ValueClassReads {
  implicit def genericReads[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                                  rv: Reads[V],
                                  vl: Validator[W, V]): Reads[W] =
    new Reads[W] {
      override def reads(json: JsValue): JsResult[W] = rv.reads(json).flatMap { v1 =>
        vl.validate(v1) match {
          case Left(e) => JsError(e)
          case Right(v2) => JsSuccess(gen.value.from(v2 :: HNil))
        }
      }
    }
}

trait ValueClassWrites {
  implicit def genericWrites[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                                   wv: Writes[V]): Writes[W] =
    new Writes[W] {
      override def writes(w: W): JsValue = wv.writes(gen.value.to(w).head)
    }
}

trait ValueClassFormats extends ValueClassReads with ValueClassWrites

object ValueClassFormats extends ValueClassFormats