package com.example.uftest1206182.datastore

import com.example.uftest1206182.models.OrderBson
import com.outr.giantscala.{Converter, DBCollection, Field, Index}

final class OrderCollection(db: Database) extends DBCollection[OrderBson]("order", db) {
  val converter: Converter[OrderBson] = Converter.auto[OrderBson]

  val details: Field[String] = Field("details")
  val status: Field[String]  = Field("status")
  val created: Field[Long]   = Field("created")
  val modified: Field[Long]  = Field("modified")
  val _id: Field[String]     = Field("_id")

  def indexes: List[Index] = List(_id.index.text.unique)
}
