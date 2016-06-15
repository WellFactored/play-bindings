package com.wellfactored.playbinders

import play.api.libs.json._
import play.api.mvc.{PathBindable, QueryStringBindable}
import shapeless.{::, Generic, HNil, Lazy}

trait GenericPathBindable {
  implicit def genericPathBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], binder: PathBindable[V], vl: Validator[W, V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, value: String): Either[String, W] =
        for {
          v <- binder.bind(key, value).right
          _ <- vl.validate(v).right
        } yield gen.value.from(v :: HNil)
    }
}

trait GenericQueryStringBindable {
  implicit def genericQueryStringBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], binder: QueryStringBindable[V], vl: Validator[W, V]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] = {
        binder.bind(key, params).map(_.right.flatMap(vl.validate).right.map(v => gen.value.from(v :: HNil)))
      }
    }
}

/**
  * `import GenericUrlBinders._` to get both the `GenericPathBindable` and `GenericQueryStringBindable`
  * generators in implicit scope. In particular, add this to your `build.sbt` to import them into
  * the routes file:
  *
  * `routesImport += "com.wellfactored.playbinders.GenericUrlBinders._"`
  */
object GenericUrlBinders extends GenericPathBindable with GenericQueryStringBindable

trait GenericReads {
  implicit def genericReads[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], rv: Reads[V], vl: Validator[W, V]): Reads[W] =
    new Reads[W] {
      override def reads(json: JsValue): JsResult[W] = rv.reads(json).flatMap { v =>
        vl.validate(v) match {
          case Left(e) => JsError(e)
          case Right(_) => JsSuccess(gen.value.from(v :: HNil))
        }
      }
    }
}

trait GenericWrites {
  implicit def genericWrites[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], wv: Writes[V]): Writes[W] =
    new Writes[W] {
      override def writes(w: W): JsValue = wv.writes(gen.value.to(w).head)
    }
}

object GenericJsonFormats extends GenericReads with GenericWrites
