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

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}
import play.api.mvc.QueryStringBindable

class ValueClassQueryStringBindableTest
  extends FlatSpec
    with Matchers
    with OptionValues
    with EitherValues
    with ValueClassQueryStringBindable
    with TestValidators {

  val goodLongValue = Map("a" -> Seq("1"))
  val badLongValue = Map("a" -> Seq("-1"))

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[QueryStringBindable[LongWrapper]]

    b.bind("a", goodLongValue).value.right.value shouldBe LongWrapper(1)
  }

  "unbind" should "extract the wrapped value and convert it to a String" in {
    val b: QueryStringBindable[LongWrapper] = implicitly[QueryStringBindable[LongWrapper]]
    val lw = LongWrapper(1337)

    b.unbind("key", lw) shouldBe "key=1337"
  }


}
