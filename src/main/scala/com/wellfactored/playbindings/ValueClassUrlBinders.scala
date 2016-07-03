package com.wellfactored.playbindings

import play.api.mvc.{PathBindable, QueryStringBindable}

trait ValueClassPathBindable extends ValueWrapperGen {
  implicit def valueClassPathBindable[W, V](implicit vw: ValueWrapper[W, V],
                                            binder: PathBindable[V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, vw.unwrap(wrapper))

      override def bind(key: String, value: String): Either[String, W] =
        for {
          v1 <- binder.bind(key, value).right
          v2 <- vw.wrap(v1).right
        } yield v2
    }
}

trait ValueClassQueryStringBindable extends ValueWrapperGen {
  implicit def valueClassQueryStringBindable[W, V](implicit vw: ValueWrapper[W, V],
                                                   binder: QueryStringBindable[V]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, vw.unwrap(wrapper))

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] = {
        binder.bind(key, params).map(_.right.flatMap(vw.wrap))
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