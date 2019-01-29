package com.example.uftest1206182.models.newtype

import io.estatico.newtype.macros.newtype

object SampleNewTypes {
  @newtype case class ATest(msg: String)
}
