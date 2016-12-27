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

import shapeless.{::, Generic, HList, HNil}

/**
  * Provide a generic function that can generate Shapeless Generic instances for
  * case classes with a single member, e.g. `Foo(s: String)` where the `Generic.Repr`
  * type is `String` rather than `String :: HNil`. From this we can build the various
  * binding types that Play uses, i.e. json `Reads` and `Writes` instances as well as
  * `PathBindable`s and `QueryStringBindable`s
  */
trait GenericValueWrapper {
  implicit def genGen[W, V, Repr <: HList](
                                            implicit
                                            gen: Generic.Aux[W, Repr],
                                            ev1: (V :: HNil) =:= Repr,
                                            ev2: Repr =:= (V :: HNil)
                                          ): Generic.Aux[W, V] = {
    new Generic[W] {
      override type Repr = V

      override def to(w: W) = gen.to(w).head

      override def from(v: V) = gen.from(v :: HNil)
    }
  }
}
