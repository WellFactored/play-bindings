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

import shapeless.{::, Generic, HNil, Lazy}

/**
  * This trait provides an implicit function that will generate a ValueWrapper[W,V]
  * for a case class of type `W` that has a single member of type `V`. This uses
  * Shapeless to summon a `Generic[W, V :: HNil]` to assist with the wrapping
  * and unwrapping, so it will actually work with any type `W` that is record-like
  * enough for Shapeless to handle.
  */
trait ValueWrapperGen {
  /**
    *
    * @param gen provides the Generic mapping between the wrapper type and the wrapped
    *            value type with optional validation of of V when wrapping.
    */
  implicit def genWV[W, V](
                            implicit
                            gen: Lazy[Generic.Aux[W, V :: HNil]]
                          ): ValueWrapper[W, V] =
    new ValueWrapper[W, V] {
      override def wrap(v: V): W = gen.value.from(v :: HNil)

      override def unwrap(w: W): V = gen.value.to(w).head
    }
}

/**
  * Import `ValueWrapperGen._` in cases where you can't or don't want to extend the
  * `ValueWrapperGen` trait yourself.
  */
object ValueWrapperGen extends ValueWrapperGen