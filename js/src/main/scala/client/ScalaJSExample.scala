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
import scala.concurrent.impl.Future
import scala.scalajs.js.typedarray.TypedArrayBuffer
import scala.scalajs.js.typedarray.ArrayBuffer
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
    val titledPane2 = TitledPane(span("Der Titel 2"), "Text 2")
    main.appendChild(div(titledPane, titledPane2).render)

    val stylesheet = App.styles.map(_.render[String]).mkString("\n");
    headContent.appendChild(scalatags.JsDom.tags2.style(stylesheet).render)
  }

}

trait Component extends Frag {
  def render = html
  val html: Node
  val css: ComponentStyle
}

trait ComponentStyle extends StyleSheet.Inline {
  App.styles += this
}

object TitledPaneStyle extends ComponentStyle{
  import dsl._

  val button = style(
    fontSize(200 %%),
    margin(12 px))
}
case class TitledPane(headline: Frag, text: String) extends Component {
  override val css = TitledPaneStyle
  override val html = Component.main

  object Component{
    import scalatags.JsDom.all._

    val main = div(Component.textDiv, clickDiv).render
    val textDiv = div(headline, div(text, css.button)).render
    val clickDiv = div("Click me", onclick := reset _).render
  }

  def reset(): Unit = {

    import autowire._ // !!!
    MyClient[AutowireApi].getAllVideos(ApiRequest(Seq(VideoField.USERNAME))).call().map{ (result: ApiResult) =>
      println("Hello world")
        for(e <- result.entities){
//          println(e.get(VideoField.USERNAME))
        }
      }

    println("blaaaaaaaggggg")

    jQuery(html).replaceWith(TitledPane.this.copy(text = if (text == "abc") "123" else "abc").render)
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