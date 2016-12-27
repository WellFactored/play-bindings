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

import com.wellfactored.valuewrapper.{ValueWrapper, ValueWrapperGen}
import play.api.libs.json._

trait ValueClassReads extends ValueWrapperGen {
  implicit def genericReads[W, V](implicit
                                  vw: ValueWrapper[W, V],
                                  rv: Reads[V]): Reads[W] = new Reads[W] {
    override def reads(json: JsValue): JsResult[W] = rv.reads(json).flatMap { v =>
      vw.wrap(v) match {
        case Left(e) => JsError(e)
        case Right(w) => JsSuccess(w)
      }
    }
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