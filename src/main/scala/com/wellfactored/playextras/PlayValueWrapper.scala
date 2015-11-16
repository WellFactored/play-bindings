
package com.wellfactored.playextras

import play.api.libs.json._
import play.api.mvc.{PathBindable, QueryStringBindable}
import shapeless._

/**
  * This trait defines implicits for the common Play Framework transformations on values. Namely:
  * - A `Reads[W]` to read from json
  * - A `Writes[W]` to write to json
  * - A `PathBindable` to convert to and from strings in the url pattern
  * - A `QueryStringBindable` to convert to and from url query parameters
  *
  * To use this with, for instance, a case class that extends AnyVal, create a companion object
  * that extends this trait with V being the primitive type (e.g. String, Int) and W being the
  * wrapped type. Then make an implicit instance of `ValueWrapper[V,W]` within the companion
  * object, defining the appropriate wrap and unwrap methods.
  */
trait PlayValueWrapper[V, W] {
  implicit def reads(implicit vr: Reads[V], w: ValueWrapper[V, W]): Reads[W] =
    PlayValueWrapper.reads[V, W]

  implicit def writes(implicit vw: Writes[V], w: ValueWrapper[V, W]): Writes[W] =
    PlayValueWrapper.writes[V, W]

  implicit def pathBindable(implicit pb: PathBindable[V], vw: ValueWrapper[V, W]): PathBindable[W] =
    PlayValueWrapper.pathBindable[V, W]

  implicit def queryStringBinder(implicit qb: QueryStringBindable[V], vw: ValueWrapper[V, W]): QueryStringBindable[W] =
    PlayValueWrapper.queryStringBindable[V, W]
}

trait SimpleWrapper[V, W] extends PlayValueWrapper[V, W] {
  def apply(v: V): W

  def unapply(w: W): Option[V]

  implicit val wraps = new ValueWrapper[V, W] {
    override def wrap(v: V): Either[String, W] = Right(apply(v))

    override def unwrap(w: W): V = unapply(w).get
  }
}

trait ShapelessWrapper[V, W] extends PlayValueWrapper[V, W] {
  def generic: shapeless.Generic[W] {type Repr = shapeless.::[V, shapeless.HNil]}

  implicit val wraps = new ValueWrapper[V, W] {
    override def wrap(v: V): Either[String, W] = Right(generic.from(v :: HNil))

    override def unwrap(w: W): V = generic.to(w).head
  }
}

/**
  * Provide functions to create various Play type class instances for types
  * that implement a ValueWrapper
  */
object PlayValueWrapper {
  def writes[V: Writes, W](implicit vw: ValueWrapper[V, W]): Writes[W] =
    new Writes[W] {
      override def writes(o: W): JsValue = implicitly[Writes[V]].writes(vw.unwrap(o))
    }

  def reads[V: Reads, W](implicit vw: ValueWrapper[V, W]): Reads[W] =
    new Reads[W] {
      override def reads(json: JsValue): JsResult[W] = implicitly[Reads[V]].reads(json).flatMap { v =>
        vw.wrap(v) match {
          case Right(w) => JsSuccess(w)
          case Left(e) => JsError(e)
        }
      }
    }

  def formats[V: Reads : Writes, W](implicit vw: ValueWrapper[V, W]): Format[W] = new Format[W] {
    override def writes(o: W): JsValue = PlayValueWrapper.writes[V, W].writes(o)

    override def reads(json: JsValue): JsResult[W] = PlayValueWrapper.reads[V, W].reads(json)
  }

  def pathBindable[V, W](implicit vb: PathBindable[V], vw: ValueWrapper[V, W]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, w: W): String = vb.unbind(key, vw.unwrap(w))

      override def bind(key: String, v: String): Either[String, W] =
        vb.bind(key, v).right.flatMap(vw.wrap)
    }

  def queryStringBindable[V, W](implicit vb: QueryStringBindable[V], vw: ValueWrapper[V, W]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, w: W): String = vb.unbind(key, vw.unwrap(w))

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] =
        vb.bind(key, params).map(_.right.flatMap(vw.wrap))
    }
}
