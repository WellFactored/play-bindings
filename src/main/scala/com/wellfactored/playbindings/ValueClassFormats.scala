package com.wellfactored.playbindings

import cats.data.Validated.{Invalid, Valid}
import com.wellfactored.valuewrapper.{ValueWrapper, ValueWrapperGen}
import play.api.libs.json._

trait ValueClassReads extends ValueWrapperGen {
  implicit def genericReads[W, V](implicit vw: ValueWrapper[W, V],
                                  rv: Reads[V]): Reads[W] =
    new Reads[W] {
      override def reads(json: JsValue): JsResult[W] = rv.reads(json).flatMap { v =>
        vw.wrap(v) match {
          case Left(e) => JsError(e)
          case Right(w) => JsSuccess(w)
        }
      }
    }
}

trait ValueClassWrites extends ValueWrapperGen {
  implicit def genericWrites[W, V](implicit vw: ValueWrapper[W, V],
                                   wv: Writes[V]): Writes[W] =
    new Writes[W] {
      override def writes(w: W): JsValue = wv.writes(vw.unwrap(w))
    }
}

trait ValueClassFormats extends ValueClassReads with ValueClassWrites

object ValueClassFormats extends ValueClassFormats