package com.example.uftest1206182.functions

import com.example.uftest1206182.models.IDModel.ID
import com.example.uftest1206182.models.Order

trait OrderRepo[F[_]] {
  type T

  def putOrder(order: Order): F[Order]
  def getOrder(orderId: ID[T]): F[Option[Order]]
  def removeOrder(orderId: ID[T]): F[Option[Order]]
}
