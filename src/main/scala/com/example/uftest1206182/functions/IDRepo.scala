package com.example.uftest1206182.functions

import com.example.uftest1206182.models.IDModel.ID

trait IDRepo[F[_]] {
  type Out

  def genId: F[ID[Out]]
}
