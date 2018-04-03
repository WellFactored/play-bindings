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

import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.mvc.PathBindable

class ValueClassPathBindableTest
  extends FlatSpec
    with Matchers
    with EitherValues
    with ValueClassPathBindable
    with TestWrappers {

  "bind" should "implicitly summon a binder for Test" in {
    val b = implicitly[PathBindable[LongWrapper]]

    b.bind("", "1").right.value shouldBe LongWrapper(1)
  }


  "unbind" should "extract the wrapped value and convert it to a String" in {
    val b = implicitly[PathBindable[LongWrapper]]
    val lw = LongWrapper(1337)

    b.unbind("", lw) shouldBe "1337"
  }
}
