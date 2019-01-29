package com.example.uftest1206182.functions.implementations

import com.example.uftest1206182.functions.IDRepo
import com.example.uftest1206182.models.IDModel._
import com.github.kolotaev.ride.Id
import io.catbird.util.Rerunnable

trait RideIDRepo extends IDRepo[Rerunnable] {
  type Out = Id

  def genId: Rerunnable[ID[Id]] = Rerunnable.const(ID(Id()))
}

object RideIDRepo extends RideIDRepo
