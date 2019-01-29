package com.example.uftest1206182.datastore

import com.example.uftest1206182.util.AppConfigLib._
import com.example.uftest1206182.util.PipeOperator._
import com.mongodb.ConnectionString
import com.outr.giantscala._
import mouse.option._

import scala.collection.JavaConverters._

final class Database
    extends MongoDatabase(
      name = getConfig[String]("MONGODB_NAME").getOrElse("orderdemo"),
      urls = {
        new ConnectionString(getConfig[String]("MONGODB_HOSTS").getOrElse("mongodb://localhost:27017")).|> { cs =>
          Option(cs.getHosts)
            .cata(
              _.asScala.toList.map(_.split(":").|>(a => MongoDBServer(a(0), a(1).toInt))),
              List(MongoDBServer.default)
            )
        }
      }
    ) {
  val order: OrderCollection = new OrderCollection(this)
}
