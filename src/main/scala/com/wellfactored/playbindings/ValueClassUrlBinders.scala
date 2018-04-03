/*
 * Copyright 2016 Well-Factored Software Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wellfactored.playbindings

import play.api.mvc.{PathBindable, QueryStringBindable}

trait ValueClassPathBindable extends ValueWrapperGen {
  implicit def valueClassPathBindable[W, V](implicit vw: ValueWrapper[W, V],
                                            binder: PathBindable[V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, vw.unwrap(wrapper))

      override def bind(key: String, value: String): Either[String, W] =
        binder.bind(key, value).right.map(vw.wrap)
    }
}

trait ValueClassQueryStringBindable extends ValueWrapperGen {
  implicit def valueClassQueryStringBindable[W, V](implicit vw: ValueWrapper[W, V],
                                                   binder: QueryStringBindable[V]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, vw.unwrap(wrapper))

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] = {
        binder.bind(key, params).map(_.right.map(vw.wrap))
      }
    }
}

trait ValueClassUrlBinders extends ValueClassPathBindable with ValueClassQueryStringBindable

/**
  * `import ValueClassUrlBinders._` to get both the `ValueClassPathBindable` and `ValueClassQueryStringBindable`
  * generators in implicit scope. In particular, add this to your `build.sbt` to import them into
  * the routes file:
  *
  * `routesImport += "com.wellfactored.playbindings.ValueClassUrlBinders._"`
  */
object ValueClassUrlBinders extends ValueClassUrlBinders