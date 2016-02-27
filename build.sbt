import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "scalatags" % "0.5.4",
  "com.github.japgolly.scalacss" %%% "core" % "0.4.0",
  "com.github.japgolly.scalacss" %%% "ext-scalatags" % "0.4.0",
  "com.lihaoyi" %%% "scalarx" % "0.3.1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.0",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
  "org.jooq" % "jooq" % "3.7.2",
  "org.jooq" % "jooq-scala" % "3.7.2",
  "org.jooq" % "jooq-codegen" % "3.7.2",
  "org.jooq" % "jooq-meta" % "3.7.2",
  "org.mariadb.jdbc" % "mariadb-java-client" % "1.3.6",
  "com.lihaoyi" %%% "upickle" % "0.3.8",
  "com.lihaoyi" %%% "autowire" % "0.2.5",
  "io.spray" % "spray-can_2.11" % "1.3.3",
  "io.spray" % "spray-routing_2.11" % "1.3.3",
  "io.spray" % "spray-http_2.11" % "1.3.3",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.2"
)

bootSnippet := "client.HelloWorld1().main(document.getElementsByTagName('head')[0], document.getElementById('content'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

EclipseKeys.withSource := true