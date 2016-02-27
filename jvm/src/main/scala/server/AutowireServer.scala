package server

import scala.concurrent.ExecutionContext.Implicits.global
import shared.AutowireApi
import shared.Name
import autowire._
import boopickle.Default._
import java.nio.ByteBuffer
import shared.Some

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
  def doThing(i: Int, s: String): Seq[String] = Seq(i.toString(), s)
  def doMoreComplex(name: Name): String = name.firstName + " " + name.lastName
  def generic(someValue: Some): String = "Hello"
}

