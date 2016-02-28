package client
import org.scalajs.dom
import dom.html
import scalajs.js.annotation.JSExport
import scalacss.DevDefaults._
import scalacss.ScalatagsCss._
import Helper._
import scalacss.mutable.Register
import scalatags.Text.TypedTag
import org.scalajs.dom.raw.HTMLStyleElement
import scalacss.{ NonEmptyVector, UnicodeRange }
import scalatags.jsdom.Frag
import rx._
import org.scalajs.jquery._
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.Element
import scalatags.generic.Modifier
import org.scalajs.dom.raw.HTMLElement
import scalajs.concurrent.JSExecutionContext.Implicits.queue
import shared._
import java.nio.ByteBuffer
import boopickle.Pickler
import scala.concurrent.impl.Future
import scala.scalajs.js.typedarray.TypedArrayBuffer
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.reflect.internal.pickling.UnPickler
import boopickle.Default._
import scala.concurrent.Future

object Helper {
  //
  //  implicit def convertComponent[T <: Component](component: T) = component.html
  //
  //  def render[T <: Component](component: T) = component.html
}

object App {

  val styles = new scala.collection.mutable.ArrayBuffer[StyleSheet.Inline]()

  object MyStyles extends StyleSheet.Inline {
    import dsl._

    val button = style(
      fontSize(200 %%),
      margin(12 px))
  }
}

@JSExport
class HelloWorld1 {

  @JSExport
  def main(headContent: html.Div, main: html.Div) = {
    import scalatags.JsDom.all._
    val titledPane = TitledPane(span("Der Titel"), "Text")
    main.appendChild(div(titledPane).render)

    val stylesheet = App.styles.map(_.render[String]).mkString("\n");
    headContent.appendChild(scalatags.JsDom.tags2.style(stylesheet).render)
  }

}

trait Component extends Frag {
  def render = xxx
  val xxx: Node
}

trait ComponentStyle extends StyleSheet.Inline {
  App.styles += this
}

case class TitledPane(headline: Frag, text: String) extends Component {

  val css = new ComponentStyle {
    import dsl._

    val button = style(
      fontSize(200 %%),
      margin(12 px))
  }

  val textDiv = {
    import scalatags.JsDom.all._
    div(headline, div(text, css.button)).render
  }

  val clickDiv = {
    import scalatags.JsDom.all._
    div("Click me", onclick := reset _).render
  }

  val xxx = {
    import scalatags.JsDom.all._
    div(textDiv, clickDiv).render
  }

  def reset(): Unit = {

    import autowire._ // !!!
    MyClient[AutowireApi].doMoreComplex(Name("Hallo", "Welt")).call().map(println(_))

    new ApiEntityRequest(UserEntityType, Seq(UserField.USERNAME), Seq(ApiFieldFilter(UserField.ID, IntApiValue(include = Seq(1, 2, 3)))))


    println("blaaaaaaaggggg")
    import scalatags.JsDom.all._
    jQuery(xxx).replaceWith(TitledPane.this.copy(text = if (text == "abc") "123" else "abc").render)
  }

}

//object MyClient extends autowire.Client[upickle.Js.Value, upickle.default.Reader, upickle.default.Writer] {
//  import upickle.default._
//  import upickle.Js
//  import autowire._
//
//  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
//  def write[Result: Writer](r: Result) = writeJs(r)
//
//  override def doCall(req: MyClient.Request) = {
//    dom.ext.Ajax.post(
//      url = "http://127.0.0.1:8080/api/" + req.path.mkString("/") + "/",
//      data = upickle.json.write(Js.Obj(req.args.toSeq: _*)))
//      .map(_.responseText)
//      .map(upickle.json.read)
//  }
//}

object MyClient extends autowire.Client[ByteBuffer, Pickler, Pickler] {
  override def doCall(req: Request): Future[ByteBuffer] = {
    dom.ext.Ajax.post(
      url = "http://127.0.0.1:8080/api/" + req.path.mkString("/") + "/",
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream")
    ).map(r => TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer]))
  }

  override def read[Result: Pickler](p: ByteBuffer) = Unpickle[Result].fromBytes(p)
  override def write[Result: Pickler](r: Result) = Pickle.intoBytes(r)
}





// uncaught exception: scala.MatchError: [Ljava.lang.String;@1 (of class [Ljava.lang.String;)