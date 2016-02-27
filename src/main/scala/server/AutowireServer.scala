package server

import autowire._
import upickle.default._
import upickle.Js
import scala.concurrent.ExecutionContext.Implicits.global

object AutowireServer extends autowire.Server[Js.Value, Reader, Writer] {
  def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.writeJs(r)

  val routes = route[AutowireApi](AutowireApiImpl)
}

trait AutowireApi {
  def doThing(i: Int, s: String): Seq[String]
}

object AutowireApiImpl extends AutowireApi {
  def doThing(i: Int, s: String): Seq[String] = Seq(i.toString(), s)
}
