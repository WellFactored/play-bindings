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

import play.api.libs.json._

trait ValueClassReads extends ValueWrapperGen {
  implicit def genericReads[W, V](implicit
                                  vw: ValueWrapper[W, V],
                                  rv: Reads[V]): Reads[W] = new Reads[W] {
    override def reads(json: JsValue): JsResult[W] = rv.reads(json).map(vw.wrap)
  }
}

trait ValueClassWrites extends ValueWrapperGen {
  implicit def genericWrites[W, V](implicit
                                   vw: ValueWrapper[W, V],
                                   wv: Writes[V]): Writes[W] = new Writes[W] {
    override def writes(w: W): JsValue = wv.writes(vw.unwrap(w))
  }
}

trait ValueClassFormats extends ValueClassReads with ValueClassWrites

object ValueClassFormats extends ValueClassFormats