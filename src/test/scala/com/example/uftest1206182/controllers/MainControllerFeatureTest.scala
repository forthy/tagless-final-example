package com.example.uftest1206182.controllers

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.example.uftest1206182.Server
import com.example.uftest1206182.docker.DockerMongodbService
import com.example.uftest1206182.models.http.CreateOrderResponse
import com.example.uftest1206182.util.PipeOperator._
import perfolation._

// sbt 'testOnly com.example.uftest1206182.controllers.MainControllerFeatureTest'
class MainControllerFeatureTest extends FeatureTest with DockerMongodbService {
  val serviceVersion: String = "0.9.9"

  override val server: EmbeddedHttpServer =
    new EmbeddedHttpServer(twitterServer = new Server, flags = Map("service.version" -> serviceVersion))

  test("Server should respond") {
    server.httpGet(path = "/tstmsg/Richard", andExpect = Ok, withJsonBody = """{"message":"Hello, Richard"}""")
    server.httpGet(path = "/tstmsg/anonymous", andExpect = Ok, withJsonBody = """{"message":"Your name, please?"}""")
    server.httpGet(path = "/tstmsg/unknown", andExpect = BadRequest)
  }

  test("Should successfully create an order") {
    server
      .httpPostJson[CreateOrderResponse](
        path = "/api/order/v1/create",
        postBody = """{"status":"P","details":"世界中の誰よりきっと CD"}""",
        andExpect = Ok
      )
      .oid
      .$$(x => info(p"Order ID: $x")) shouldNot be("")
  }

  override def afterAll(): Unit = {
    server.close()

    super.afterAll()
  }
}
