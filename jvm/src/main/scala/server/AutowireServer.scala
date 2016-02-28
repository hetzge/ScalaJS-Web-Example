package server

import scala.concurrent.ExecutionContext.Implicits.global
import shared.{ApiResult, AutowireApi}
import autowire._
import boopickle.Default._
import java.nio.ByteBuffer

//object AutowireServer extends autowire.Server[Js.Value, Reader, Writer] {
//  def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
//  def write[Result: Writer](r: Result) = upickle.default.writeJs(r)
//
//  val routes = route[AutowireApi](AutowireApiImpl)
//}

object AutowireServer extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)
}

object AutowireApiImpl extends AutowireApi {

}

