package com.wellfactored.playbindings

import play.api.mvc.{PathBindable, QueryStringBindable}
import shapeless.{Generic, HNil, Lazy, ::}

trait ValueClassPathBindable {
  implicit def valueClassPathBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                                            binder: PathBindable[V],
                                            vl: Validator[W, V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, value: String): Either[String, W] =
        for {
          v1 <- binder.bind(key, value).right
          v2 <- vl.validate(v1).right
        } yield gen.value.from(v2 :: HNil)
    }
}

trait ValueClassQueryStringBindable {
  implicit def valueClassQueryStringBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]],
                                                   binder: QueryStringBindable[V],
                                                   vl: Validator[W, V]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] = {
        binder.bind(key, params)
          .map(_.right.flatMap(vl.validate)
            .right.map(v => gen.value.from(v :: HNil)))
      }
    }
}


trait ValueClassUrlBinders extends ValueClassPathBindable with ValueClassQueryStringBindable

/**
  * `import ValueClassUrlBinders._` to get both the `ValueClassPathBindable` and `ValueClassQueryStringBindable`
  * generators in implicit scope. In particular, add this to your `build.sbt` to import them into
  * the routes file:
  *
  * `routesImport += "com.wellfactored.playbinders.ValueClassUrlBinders._"`
  */
object ValueClassUrlBinders extends ValueClassUrlBinders