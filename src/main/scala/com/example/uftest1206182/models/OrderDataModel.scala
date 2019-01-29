package com.example.uftest1206182.models

import com.example.uftest1206182.models.IDModel.ID
import com.github.kolotaev.ride.Id
import com.outr.giantscala.ModelObject
import com.twitter.util.Time

sealed trait OrderStatus extends Product with Serializable
object OrderStatus {
  case object Approved  extends OrderStatus
  case object Placed    extends OrderStatus
  case object Shipped   extends OrderStatus
  case object Delivered extends OrderStatus

  implicit class OrderStatusOps(s: OrderStatus) {
    def unsafeStatus: String = s match {
      case Approved  => "A"
      case Placed    => "P"
      case Shipped   => "S"
      case Delivered => "D"
    }
  }

  def safeStatus(statusString: String): OrderStatus = statusString match {
    case "A" => Approved
    case "P" => Placed
    case "S" => Shipped
    case "D" => Delivered
  }
}

final case class Order(id: ID[Id], status: OrderStatus, details: String)

final case class OrderBson(_id: String,
                           status: String,
                           details: String,
                           created: Long = Time.now.inMillis,
                           modified: Long = Time.now.inMillis)
    extends ModelObject

case object InvalidStatus extends RuntimeException("invalid status")
