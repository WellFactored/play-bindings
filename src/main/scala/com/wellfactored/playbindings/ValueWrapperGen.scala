/*
 * Copyright (C) 2016  Well-Factored Software Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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