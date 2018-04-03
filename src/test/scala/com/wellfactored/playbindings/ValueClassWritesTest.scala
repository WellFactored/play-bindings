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

import org.scalatest.{Matchers, WordSpecLike}
import play.api.libs.json.{JsNumber, Json, OWrites, Writes}

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

      implicit val fWrites: OWrites[Foo] = Json.writes[Foo]

      fWrites.writes(Foo(LongWrapper(1337), "leet")) shouldBe Json.parse("""{ "l" : 1337, "s" : "leet" }""")
    }
  }
}
