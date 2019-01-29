package com.example.uftest1206182.integration

import com.example.uftest1206182.functions.implementations.RideIDRepo
import com.example.uftest1206182.modules.IDRepoModule
import com.twitter.inject.app.TestInjector
import com.twitter.inject.{Injector, IntegrationTest}
import cats.syntax.apply._
import com.twitter.util.Await

// sbt "testOnly com.example.uftest1206182.integration.IDRepoModuleTest"
class IDRepoModuleTest extends IntegrationTest {
  protected def injector: Injector = TestInjector(modules = Seq(IDRepoModule)).create

  test("[Ride]IDRepo should successfully generate unique IDs") {
    val idRepo = injector.instance[RideIDRepo]

    Await.result((idRepo.genId, idRepo.genId).mapN((x, y) => x != y).run) shouldBe true
  }
}
