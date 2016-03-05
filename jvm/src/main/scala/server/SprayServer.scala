package server

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.{MediaTypes, HttpEntity}
import shared.AutowireApi
import java.nio.ByteBuffer
import spray.http.HttpHeaders

object SprayServer extends SimpleRoutingApp with App {

  val AccessControlAllowAll = HttpHeaders.RawHeader(
    "Access-Control-Allow-Origin", "*")
  val AccessControlAllowHeadersAll = HttpHeaders.RawHeader(
    "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")

  implicit val system = ActorSystem()

  import upickle.default._
  import shared._
  startServer("127.0.0.1", port = 8080) {
    options {
      respondWithHeaders(AccessControlAllowAll, AccessControlAllowHeadersAll) {
        complete("wuff")
      }
    } ~
      post {
        path("api" / Segments /) { path =>
          extract(_.request.entity.asString) { value =>
            complete {
              AutowireServer.route[AutowireApi](AutowireApiImpl)(
                autowire.Core.Request(path, upickle.default.read[Map[String, String]](value))
              )
            }
          }
        }
      }
  }

}