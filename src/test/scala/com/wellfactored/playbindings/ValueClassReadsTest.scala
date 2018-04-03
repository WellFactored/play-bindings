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

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._

class ValueClassReadsTest extends FlatSpec with Matchers with ValueClassReads with TestWrappers {
  "reads" should "implicitly summon a Reads for LongWrapper" in {
    val r = implicitly[Reads[LongWrapper]]

    r.reads(JsNumber(1)) shouldBe JsSuccess(LongWrapper(1))
  }
}
