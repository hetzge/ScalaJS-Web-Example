import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

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
  "com.lihaoyi" %%% "autowire" % "0.2.5"
)

bootSnippet := "example.HelloWorld1().main(document.getElementsByTagName('head')[0], document.getElementById('content'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

