import sbt.Keys._
import com.amazonaws.regions.{Region, Regions}
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.DockerAlias

import scala.language.postfixOps

fork in run := true

resolvers += Resolver.sonatypeRepo("releases")
resolvers += "maven.twttr.com" at "https://maven.twttr.com"
resolvers += Resolver.jcenterRepo

enablePlugins(
  JavaAppPackaging,
  DockerPlugin,
  GitVersioning,
  GitBranchPrompt,
  DockerContainerPlugin,
  MicrositesPlugin,
  EcrPlugin
)

initialCommands in console := """
                | import com.twitter.util.{Future, FuturePool, Await}
                |""".stripMargin

lazy val rootProject = project
  .in(file("."))
  .settings(name := "tagless-final-sample", organization := "com.example", version := "0.0.3", scalaVersion := "2.12.8")

lazy val docs = project
  .in(file("tagless-example-docs"))
  .settings(mdocVariables := Map("VERSION" -> (version in rootProject).value))
  .dependsOn(rootProject)
  .enablePlugins(MdocPlugin)

coverageHighlighting := true

coverageMinimum := 70
coverageFailOnMinimum := true
coverageExcludedPackages := ".*sse*.;.*util*.;.*client*."

scapegoatVersion in ThisBuild := "1.3.8"

scalafmtConfig := Some(file(".scalafmt.conf"))
scalafmtOnCompile := true

autoCompilerPlugins := true
addCompilerPlugin("com.criteo.socco" %% "socco-plugin"       % "0.1.9")
addCompilerPlugin("com.olegpy"       %% "better-monadic-for" % "0.3.0-M4")
addCompilerPlugin("com.github.cb372" %% "scala-typed-holes"  % "0.0.3")
addCompilerPlugin(("io.tryp" % "splain" % "0.3.5").cross(CrossVersion.patch))
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

lazy val versions = new {
  val finatra        = "19.1.0"
  val guice          = "4.2.2"
  val logback        = "1.2.3"
  val mockito        = "1.10.19"
  val scalatest      = "3.0.5"
  val junitInterface = "0.11"
  val dockerItScala  = "0.10.0-beta4"
  val scalaUri       = "1.3.1"
  val hamsters       = "2.6.0"
  val fluentdScala   = "0.2.5"
  val swaggerFinatra = "18.12.0"
  val wireMock       = "2.20.0"
  val catbird        = "19.1.0"
  val scalaErrors    = "1.2"
  val perfolation    = "1.0.4"
  val mouse          = "0.20"
  val ride           = "1.0.1"
  val giantMongoDB   = "1.2.13"
  val monix          = "3.0.0-RC2"
  val scalaCache     = "0.27.0"
  val meowMTL        = "0.2.0"
  val newtype        = "0.4.2"
}

libraryDependencies ++= Seq(
  "io.estatico"                   %% "newtype"                        % versions.newtype,
  "com.olegpy"                    %% "meow-mtl"                       % versions.meowMTL,
  "com.github.cb372"              %% "scalacache-core"                % versions.scalaCache,
  "com.github.cb372"              %% "scalacache-caffeine"            % versions.scalaCache,
  "io.monix"                      %% "monix-execution"                % versions.monix,
  "com.outr"                      %% "giant-scala"                    % versions.giantMongoDB,
  "com.github.kolotaev"           %% "ride"                           % versions.ride,
  "com.jakehschwartz"             %% "finatra-swagger"                % versions.swaggerFinatra,
  "org.typelevel"                 %% "mouse"                          % versions.mouse,
  "com.outr"                      %% "perfolation"                    % versions.perfolation,
//  "com.github.mehmetakiftutuncu"  %% "errors"                         % versions.scalaErrors,
//  "io.catbird"                    %% "catbird-finagle"                % versions.catbird,
//  "io.catbird"                    %% "catbird-effect"                 % versions.catbird,
//  "com.github.tomakehurst"        % "wiremock"                        % versions.wireMock,
//  "eu.inn"                        %% "fluentd-scala"                  % versions.fluentdScala,
//  "io.github.scala-hamsters"      %% "hamsters"                       % versions.hamsters,
  "io.lemonlabs"                  %% "scala-uri"                      % versions.scalaUri,
  "com.twitter"                   %% "finatra-http"                   % versions.finatra,
//  "com.twitter"                   %% "finatra-httpclient"             % versions.finatra,
  "com.twitter"                   %% "finatra-jackson"                % versions.finatra,
  "ch.qos.logback"                % "logback-classic"                 % versions.logback,
  "com.twitter"                   %% "twitter-server-logback-classic" % versions.finatra,
  "com.twitter"                   %% "finatra-http"                   % versions.finatra % "test",
  "com.twitter"                   %% "finatra-jackson"                % versions.finatra % "test",
  "com.twitter"                   %% "inject-server"                  % versions.finatra % "test",
  "com.twitter"                   %% "inject-app"                     % versions.finatra % "test",
  "com.twitter"                   %% "inject-core"                    % versions.finatra % "test",
  "com.twitter"                   %% "inject-modules"                 % versions.finatra % "test",
  "com.google.inject.extensions"  % "guice-testlib"                   % versions.guice % "test",
  "com.twitter"                   %% "finatra-http"                   % versions.finatra % "test" classifier "tests",
  "com.twitter"                   %% "finatra-jackson"                % versions.finatra % "test" classifier "tests",
  "com.twitter"                   %% "inject-server"                  % versions.finatra % "test" classifier "tests",
  "com.twitter"                   %% "inject-app"                     % versions.finatra % "test" classifier "tests",
  "com.twitter"                   %% "inject-core"                    % versions.finatra % "test" classifier "tests",
  "com.twitter"                   %% "inject-modules"                 % versions.finatra % "test" classifier "tests",
  "org.mockito"                   % "mockito-core"                    % versions.mockito % "test",
  "org.scalatest"                 %% "scalatest"                      % versions.scalatest % "test",
  "com.novocode"                  % "junit-interface"                 % versions.junitInterface % "test",
  "com.whisk"                     %% "docker-testkit-scalatest"       % versions.dockerItScala % "test",
  "com.whisk"                     %% "docker-testkit-core"            % versions.dockerItScala % "test",
  "com.chuusai"                   %% "shapeless"                      % "2.3.3",
  "com.github.ben-manes.caffeine" % "caffeine"                        % "2.6.2",
  "com.google.inject"             % "guice"                           % "4.0",
  "com.outr"                      %% "profig"                         % "2.3.4",
  "com.outr"                      %% "profig-macros"                  % "2.3.4",
  "com.twitter"                   %% "finagle-base-http"              % "19.1.0",
  "com.twitter"                   %% "finagle-core"                   % "19.1.0",
  "com.twitter"                   %% "finagle-http"                   % "19.1.0",
  "com.twitter"                   %% "finatra-utils"                  % "19.1.0",
  "com.twitter"                   %% "inject-app"                     % "19.1.0",
  "com.twitter"                   %% "inject-core"                    % "19.1.0",
  "com.twitter"                   %% "inject-slf4j"                   % "19.1.0",
  "com.twitter"                   %% "util-app"                       % "19.1.0",
  "com.twitter"                   %% "util-core"                      % "19.1.0",
  "com.twitter"                   %% "util-slf4j-api"                 % "19.1.0",
  "io.catbird"                    %% "catbird-util"                   % "19.1.0",
  "io.circe"                      %% "circe-core"                     % "0.10.1",
  "io.circe"                      %% "circe-generic"                  % "0.10.1",
  "io.circe"                      %% "circe-generic-extras"           % "0.10.1",
  "io.swagger"                    % "swagger-models"                  % "1.5.21",
  "javax.inject"                  % "javax.inject"                    % "1",
  "org.mongodb"                   % "bson"                            % "3.9.0",
  "org.mongodb"                   % "mongodb-driver-core"             % "3.9.0",
  "org.mongodb.scala"             %% "mongo-scala-bson"               % "2.5.0",
  "org.scala-lang"                % "scala-reflect"                   % "2.12.8",
  "org.typelevel"                 %% "cats-core"                      % "1.5.0"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

fork in test := false

parallelExecution in Test := false

clippyColorsEnabled := true

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-Xcheckinit",
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match", // Pattern match may not be typesafe.
  "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ypartial-unification", // Enable partial unification in type constructor inference
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-P:clippy:colors=true",
  "-Ycache-plugin-class-loader:last-modified",
  "-Ycache-macro-class-loader:last-modified",
  "-Ybackend-parallelism",
  s"${sys.runtime.availableProcessors() * 2}",
  "-Ybackend-worker-queue",
  "8",
  "-P:bm4:no-filtering:y",
  "-P:bm4:no-map-id:y",
  "-P:bm4:no-tupling:y",
  "-P:socco:out:./target/socco",
  "-P:socco:package_com.twitter.util:https://twitter.github.io/util/docs/",
  "-P:socco:package_scala:http://www.scala-lang.org/api/current/",
  "-P:socco:package_com.htc.vr8.:file://./target/scala-2.2/api/",
  "-P:splain:all:true"
)

testReportFormats := Set(WhiteSpaceDelimited, Html, Json)

// bashScriptExtraDefines += """addJava "-Dnetworkaddress.cache.ttl=60""""
bashScriptExtraDefines ++= Seq(
  """addJava "-server"""",
  """addJava "-Dnetworkaddress.cache.ttl=60"""",
  """addJava "-XX:+UnlockExperimentalVMOptions"""",
  """addJava "-XX:+EnableJVMCI"""",
  """addJava "-XX:+UseJVMCICompiler"""",
  """addJava "-XX:+UseCGroupMemoryLimitForHeap"""",
  """addJava "-XX:+UseG1GC"""",
  """addJava "-XX:+UseStringDeduplication""""
)
bashScriptExtraDefines ++= Seq(
  """addApp "-log.level=${LOG_LEVEL:-INFO}"""",
  """addApp "-swagger.docs.endpoint=${SWAGGER_DOC_PATH:-/uftest1206182/docs}"""",
  s"""addApp "-service.version=${version.value}""""
)

val gitHeadCode = SettingKey[String]("git-head-hash", "The commit hash code of HEAD")
gitHeadCode := git.gitHeadCommit.value
  .map { sha =>
    s"${sha.take(7)}"
  }
  .getOrElse("na")

dockerVersion := Some(DockerVersion(17, 9, 1, Some("ce")))
defaultLinuxInstallLocation in Docker := "/opt/tagless-final-example"
packageName in Docker := "vr/finatra-sample-service"
// dockerBaseImage := "openjdk:8-jre-slim"
dockerBaseImage := "findepi/graalvm:1.0.0-rc10"
version in Docker := s"${if (gitHeadCode.value != "na") s"${version.value}_${gitHeadCode.value}" else version.value}"
maintainer in Docker := "Richard Chuo <richard_chuo@htc.com>"
dockerExposedPorts := Seq(9999, 9990)
dockerAlias := DockerAlias(None, None, (packageName in Docker).value, Some((version in Docker).value))
dockerUpdateLatest := false
dockerBuildOptions := Seq(
  "--force-rm",
  "-t",
  s"${(packageName in Docker).value}:${(version in Docker).value}",
  "--no-cache",
  "--pull"
)
dockerCommands := dockerCommands.value.take(1) ++ Seq(
  Cmd("LABEL", s"version=${version.value}"),
  Cmd("LABEL", "owner_team=architect"),
  Cmd("LABEL", s"""build_id=${Option(System.getProperty("build_id")).getOrElse("NA")}"""),
  Cmd("ENV", "DOCKER_CONTENT_TRUST=1", "SERVICE_NAME=tagless-final-example SERVICE_TAGS=vr,service")
) ++ dockerCommands.value.drop(1)

// AWS ECR support
region in Ecr := Region.getRegion(Regions.US_WEST_2)
repositoryName in Ecr := (packageName in Docker).value
localDockerImage in Ecr := (packageName in Docker).value + ":" + (version in Docker).value
repositoryTags in Ecr := Seq((version in Docker).value)
push in Ecr := ((push in Ecr) dependsOn (publishLocal in Docker, createRepository in Ecr, login in Ecr)).value
publish in Docker := (push in Ecr).value

// MicroSites
micrositeName := "tagless-final-example"
micrositeBaseUrl := "tfe"
micrositeDocumentationUrl := "/tfe/docs"
micrositeAuthor := "Richard Chuo"
micrositeOrganizationHomepage := "http://www.htc.com"
micrositeGitHostingService := Other("HICHub")
micrositeGitHostingUrl := "https://hichub.htc.com"

// License report style
//licenseReportStyleRules := Some("table, th, td {border: 1px solid grey;}")
