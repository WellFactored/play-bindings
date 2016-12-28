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

import org.scalatest.{Matchers, WordSpecLike}
import play.api.libs.json.{JsNumber, Json, Writes}

object testClasses {

  case class LongWrapper(l: Long) extends AnyVal

  case class Foo(l: LongWrapper, s: String)

}

class ValueClassWritesTest extends WordSpecLike with Matchers with ValueClassWrites {

  import testClasses.LongWrapper

  "writes" should {
    "implicitly summon a Writes for LongWrapper" in {
      val w = implicitly[Writes[LongWrapper]]

      w.writes(LongWrapper(1337)) shouldBe JsNumber(1337)
    }

    "implicitly summon a Writes for LongWrapper when constructing a Writes for a case class" in {
      import testClasses.Foo

      implicit val fWrites = Json.writes[Foo]
    }
  }
}
