package server

import upickle.default._
import upickle.Js
import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.{ MediaTypes, HttpEntity }

object SprayServer extends SimpleRoutingApp with App {

  implicit val system = ActorSystem()
  startServer("127.0.0.1", port = 8080) {
    post {
      path("api" / Segments /) { path =>
        extract(_.request.entity.asString) { entityString =>
          complete {
            AutowireServer.route[AutowireApi](AutowireApiImpl)(
              autowire.Core.Request(path, upickle.json.read(entityString).asInstanceOf[Js.Obj].value.toMap))
              .map(upickle.json.write(_))
          }
        }
      }
    }
  }

}