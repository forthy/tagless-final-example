package com.example.uftest1206182.functions.implementations

import com.example.uftest1206182.datastore.Database
import com.example.uftest1206182.functions.OrderRepo
import com.example.uftest1206182.models.IDModel.ID
import com.example.uftest1206182.models.{Order, OrderBson}
import com.github.kolotaev.ride.Id
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}
import com.example.uftest1206182.models.OrderStatus._
import io.github.hamsters.twitter.Implicits._
import perfolation._
import com.example.uftest1206182.ServerMain._
import com.twitter.inject.Logging
import mouse.option._
import scalacache._
import scalacache.modes.sync._
import scala.concurrent.duration._
import cats.syntax.option._
import com.example.uftest1206182.util.AppConfigLib._

@Singleton
class MongoDBOrderRepo @Inject()(db: Database, cache: Cache[Order]) extends OrderRepo[Rerunnable] with Logging {
  import db.order._

  private implicit val c: Cache[Order] = cache
  private val ttl                      = getConfig[Int]("CACHE_TTL").getOrElse(60).minutes.some

  type T = Id

  def put(order: Order): Rerunnable[Order] =
    Rerunnable.fromFuture(insert(toBson(order)).toTwitterFuture).flatMap {
      case Right(ob) =>
        Rerunnable {
          sync.put(ob._id)(order, ttl)

          order
        }
      case Left(e) => Rerunnable.raiseError(new Error(p"Order insert failed - reason:[${e.`type`.code}]", e.throwable))
    }

  def get(orderId: ID[Id]): Rerunnable[Option[Order]] =
    Rerunnable(sync.get(orderId.id.toString())).flatMap(
      _.cata(
        o => Rerunnable.const(o.some),
        Rerunnable.fromFuture(
          aggregate.`match`(_id === orderId.id.toString()).toFuture.map(_.headOption.map(toOrder)).handle {
            case e: Throwable =>
              // DEBUG
              warn(p"Order - id:[${orderId.id.toString()}] search failed - reason:[${e.getMessage}]")

              None
          }
        )
      )
    )

  def remove(orderId: ID[Id]): Rerunnable[Option[Order]] =
    get(orderId).flatMap(
      orderOpt =>
        orderOpt
          .map { o =>
            sync.remove(orderId.id.toString())
            Rerunnable.fromFuture(delete(orderId.id.toString()).map(_.map(_ => o.some)))
          }
          .cata(_.map {
            case Right(r) => r
            case Left(e)  =>
              // DEBUG
              warn(p"Order delete failed - reason:[${e.`type`.code}]", e.throwable)

              None
          }, Rerunnable.const(None))
    )

  private[this] def toBson(order: Order): OrderBson =
    OrderBson(_id = order.id.id.toString(), status = order.status.unsafeStatus, details = order.details)

  private[this] def toOrder(oBson: OrderBson): Order = Order(ID(Id(oBson._id)), safeStatus(oBson.status), oBson.details)
}
