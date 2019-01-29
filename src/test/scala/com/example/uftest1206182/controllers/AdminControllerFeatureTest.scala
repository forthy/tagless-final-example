package com.example.uftest1206182.controllers

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.http.Status._
import com.example.uftest1206182.Server
import com.example.uftest1206182.docker.DockerMongodbService

class AdminControllerFeatureTest extends FeatureTest with DockerMongodbService {
  val serviceVersion = "0.9.9"

  override val server =
    new EmbeddedHttpServer(twitterServer = new Server, flags = Map("service.version" -> serviceVersion))

  test("Server should return `OK` (health check) if the service is on") {
    server.httpGet(path = "/health", andExpect = Ok)
  }

  test("Server should return service version") {
    server.httpGet(path = "/version", andExpect = Ok, withBody = serviceVersion)
  }

  override def afterAll(): Unit = {
    server.close()

    super.afterAll()
  }
}
