package com.example.uftest1206182

import com.example.uftest1206182.controllers.{AdminController, MainController}
import com.example.uftest1206182.filters.CommonFilters
import com.example.uftest1206182.modules.{IDRepoModule, MongoDBModule, ServiceSwaggerModule}
import com.example.uftest1206182.util.AppConfigLib._
import com.jakehschwartz.finatra.swagger.DocsController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.util.Var
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService
import perfolation._
import com.example.uftest1206182.util.PipeOperator._

object ServerMain extends Server

class Server extends HttpServer {
  val health = Var("good")

  implicit lazy val scheduler: SchedulerService = Scheduler.io("test-service")

  override protected def modules = Seq(ServiceSwaggerModule, IDRepoModule, MongoDBModule)

  override def defaultHttpPort = getConfig[String]("FINATRA_HTTP_PORT").fold(":9999")(x => p":$x")
  override val name            = "com.example.uftest1206182-Uftest1206182"

  override def configureHttp(router: HttpRouter): Unit =
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[DocsController]
      .add[AdminController]
      .add[MainController]
      .|>(_ => ())
}
