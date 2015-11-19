package com.wellfactored.playextras

import play.api.libs.json._

case class TableId(id: Long) extends AnyVal

object TableId

object Test {
  val i = TableId(5)

  val pvw = PlayBindings[Long, TableId]()
  import pvw._

  Json.toJson(i)

}

