package com.example.uftest1206182

import com.example.uftest1206182.docker.DockerMongodbService
import com.google.inject.Stage
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class StartupTest extends FeatureTest with DockerMongodbService {

  val server = new EmbeddedHttpServer(stage = Stage.PRODUCTION, twitterServer = new Server)

  test("server") {
    server.assertHealthy()
  }

  override def afterAll(): Unit = {
    server.close()

    super.afterAll()
  }
}
