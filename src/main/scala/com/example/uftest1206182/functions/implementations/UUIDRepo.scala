package com.example.uftest1206182.functions.implementations

import java.util.UUID

import com.example.uftest1206182.functions.IDRepo
import com.example.uftest1206182.models.IDModel.ID
import io.catbird.util.Rerunnable

trait UUIDRepo extends IDRepo[Rerunnable] {
  type Out = UUID

  def genId: Rerunnable[ID[UUID]] = Rerunnable.const(ID(UUID.randomUUID()))
}

object UUIDRepo extends UUIDRepo
