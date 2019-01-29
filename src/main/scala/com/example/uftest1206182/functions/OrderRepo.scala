package com.example.uftest1206182.functions

import com.example.uftest1206182.models.IDModel.ID
import com.example.uftest1206182.models.Order

trait OrderRepo[F[_]] {
  type T

  def put(order: Order): F[Order]
  def get(orderId: ID[T]): F[Option[Order]]
  def remove(orderId: ID[T]): F[Option[Order]]
}
