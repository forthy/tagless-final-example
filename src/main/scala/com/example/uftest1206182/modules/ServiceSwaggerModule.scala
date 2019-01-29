package com.example.uftest1206182.modules

import com.google.inject.Provides
import com.jakehschwartz.finatra.swagger.SwaggerModule
import io.swagger.models.{Contact, Info, Swagger}
import io.swagger.models.auth.BasicAuthDefinition

object ServiceSwaggerModule extends SwaggerModule {
  val swaggerUI      = new Swagger()
  val serviceVersion = flag[String]("service.version", "NA", "the version of service")

  @Provides
  def swagger: Swagger = {

    val info = new Info()
      .contact(new Contact().name("Richard Chuo").email("richard_chuo@htc.com"))
      .description("**uftest-120618-2** - A service that serves as a service project template.")
      .version(serviceVersion())
      .title("uftest-120618-2 API")

    swaggerUI
      .info(info)
      .addSecurityDefinition("sampleBasic", {
        val d = new BasicAuthDefinition()
        d.setType("basic")
        d
      })

    swaggerUI
  }
}
