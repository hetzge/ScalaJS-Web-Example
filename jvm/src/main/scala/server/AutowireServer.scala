package server

import scala.concurrent.ExecutionContext.Implicits.global
import shared.{ApiRequest, ApiResult, AutowireApi}
import autowire._

//object AutowireServer extends autowire.Server[Js.Value, Reader, Writer] {
//  def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
//  def write[Result: Writer](r: Result) = upickle.default.writeJs(r)
//
//  val routes = route[AutowireApi](AutowireApiImpl)
//}

import upickle.default._
import shared._

object AutowireServer extends autowire.Server[String, Reader, Writer] {
  override def read[R: Reader](p: String) = upickle.default.read[R](p)
  override def write[R: Writer](r: R) = upickle.default.write(r)
}

object AutowireApiImpl extends AutowireApi {
  override def getAllVideos(apiRequest: ApiRequest): ApiResult = Server.dbService.getVideos(apiRequest)
}

