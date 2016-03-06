package client

import org.scalajs.dom
import dom.html
import scalacss.ext.CssReset
import scalajs.js.annotation.JSExport
import scalacss.DevDefaults._
import scalacss.ScalatagsCss._
import scalacss.mutable.Register
import scalatags.Text.TypedTag
import org.scalajs.dom.raw.HTMLStyleElement
import scalacss.{ NonEmptyVector, UnicodeRange }
import scalatags.jsdom.Frag
import org.scalajs.jquery._
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.Element
import scalatags.generic.Modifier
import org.scalajs.dom.raw.HTMLElement
import shared._
import java.nio.ByteBuffer
import scala.concurrent.impl.Future
import scala.scalajs.js.typedarray.TypedArrayBuffer
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.queue

object App {
  val styles = new scala.collection.mutable.ArrayBuffer[StyleSheet.Inline]()
}

@JSExport
class HelloWorld1 {

  @JSExport
  def main(headContent: html.Div, main: html.Div) = {
    import scalatags.JsDom.all._
    val x = Model[Int](10)

    main.appendChild(div(Component.DemoElement()).render)

    println("stylecount", App.styles.size)



    val stylesheet = App.styles.map(_.render[String]).mkString("\n");


    headContent.appendChild(scalatags.JsDom.tags2.style(stylesheet).render)
  }

}

import upickle.default._

object MyClient extends autowire.Client[String, Reader, Writer] {

  override def doCall(req: Request): Future[String] = {
    println(req.args)

    dom.ext.Ajax.post(
      url = "http://127.0.0.1:8080/api/" + req.path.mkString("/") + "/",
      data = upickle.default.write(req.args),
      responseType = "application/json",
      headers = Map("Content-Type" -> "application/json")
    ).map(_.responseText)
  }

  override def read[R: Reader](p: String) = upickle.default.read[R](p)
  override def write[R: Writer](r: R) = upickle.default.write[R](r)
}





// uncaught exception: scala.MatchError: [Ljava.lang.String;@1 (of class [Ljava.lang.String;)