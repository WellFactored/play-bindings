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

/**
  * `ValueWrapper` is a type class that abstracts the creation of a class that
  * wraps a single value of another type, as well as the action of extracting
  * the wrapped value from the wrapper. For example, for a single-member case class
  * the `wrap` method would use the companion object `apply` method to construct
  * an instance and the accessor for the member to extract the wrapped value.
  *
  * @tparam W the type of the wrapper
  * @tparam V the type of the wrapped value
  */
trait ValueWrapper[W, V] {
  /**
    * Wrap the value `v` in an instance of type 'W'
    */
  def wrap(v: V): W

  /**
    * Extract and return the value of type `V` from the instance of `W`
    */
  def unwrap(w: W): V
}
