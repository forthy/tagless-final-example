package com.example.uftest1206182.controllers

import cats.syntax.option._
import com.example.uftest1206182.functions.implementations.{MongoDBOrderRepo, RideIDRepo, UUIDRepo}
import com.example.uftest1206182.models.Order
import com.example.uftest1206182.models.OrderStatus._
import com.example.uftest1206182.models.http.{CreateOrderRequest, CreateOrderResponse, GreetingsRequest, IDResponse}
import com.example.uftest1206182.services.SampleMessageService
import com.jakehschwartz.finatra.swagger.SwaggerController
import com.twitter.finagle.http.Request
import io.swagger.models.Swagger
import javax.inject.{Inject, Singleton}

@Singleton
class MainController @Inject()(
    s: Swagger,
    msgSvc: SampleMessageService,
    idRepo: RideIDRepo,
    uuidRepo: UUIDRepo,
    orderRepo: MongoDBOrderRepo
) extends SwaggerController {
  implicit protected val swagger = s

  getWithDoc("/tstmsg/:name") { o =>
    o.summary("Acquiring greetings message")
      .tag("Greetings")
      .routeParam[String]("name", "Greetings name")
      .responseWith(200, "Hello message")
  } { request: GreetingsRequest =>
    msgSvc(request.name).map(response.ok.json).run.handle {
      case _: Error => response.badRequest
    }
  }

  getWithDoc("/genid/ride") { o =>
    o.summary("Generate a Ride ID")
      .tag("Id")
      .responseWith[IDResponse](200, "An unique ID", IDResponse("b8uhqvioith6uqnvvvq0").some)
  } { _: Request =>
    idRepo.genId.map(i => response.ok.json(IDResponse(i.id.toString))).run
  }

  getWithDoc("/genid/uuid") { o =>
    o.summary("Generate an UUID ID")
      .tag("Id")
      .responseWith[IDResponse](200, "An unique ID", IDResponse("662a83aa-f9e4-11e8-95ff-1a00d570c101").some)
  } { _: Request =>
    uuidRepo.genId.map(i => response.ok.json(IDResponse(i.id.toString))).run
  }

  postWithDoc("/api/order/v1/create") { o =>
    o.summary("Create an order")
      .tag("Order")
      .description("This API creates an order in the system")
      .bodyParam[CreateOrderRequest]("Order JSON payload")
      .responseWith[String](200, "Order creation succeeded")
      .responseWith[String](400, "The order status can be improper")
      .responseWith[String](500, "A situation that the service cannot handle")
  } { req: CreateOrderRequest =>
    (for {
      oid <- idRepo.genId
      _   <- orderRepo.putOrder(Order(oid, safeStatus(req.status), req.details))
    } yield response.ok.json(CreateOrderResponse(oid.id.toString()))).run
  }
}
