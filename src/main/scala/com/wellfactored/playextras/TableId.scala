package com.wellfactored.playextras

import play.api.libs.json.Json

case class TableId(id: Long) extends AnyVal

object TableId extends SimpleWrapper[Long, TableId]

object Test {
  val i = TableId(5)




  Json.toJson(i)

}

