package com.wellfactored.playextras

import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{PathBindable, QueryStringBindable}
import shapeless.{::, Generic, HNil}

trait PlayBindings[V, W] {
  implicit def wraps: ValueWrapper[V, W]

  implicit def reads(implicit vr: Reads[V]): Reads[W]

  implicit def writes(implicit vw: Writes[V]): Writes[W]

  implicit def pathBindable(implicit pb: PathBindable[V]): PathBindable[W]

  implicit def queryStringBinder(implicit qb: QueryStringBindable[V]): QueryStringBindable[W]
}

object PlayBindings {
  def apply[V, W](implicit gen: Generic.Aux[W, (V :: HNil)]): PlayBindings[V, W] = {

    new PlayBindings[V, W] {
      override implicit val wraps = new ValueWrapper[V, W] {
        override def wrap(v: V): Either[String, W] = Right(gen.from(v :: HNil))

        override def unwrap(w: W): V = gen.to(w).head
      }

      implicit def reads(implicit vr: Reads[V]): Reads[W] = PlayValueWrapper.reads[V, W]

      implicit def writes(implicit vw: Writes[V]): Writes[W] = PlayValueWrapper.writes[V, W]

      implicit def pathBindable(implicit pb: PathBindable[V]): PathBindable[W] = PlayValueWrapper.pathBindable[V, W]

      implicit def queryStringBinder(implicit qb: QueryStringBindable[V]): QueryStringBindable[W] =
        PlayValueWrapper.queryStringBindable[V, W]
    }
  }
}