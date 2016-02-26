package example
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
    println("blaaa")
    import scalatags.JsDom.all._
    jQuery(xxx).replaceWith(TitledPane.this.copy(text = if (text == "abc") "123" else "abc").render)
  }

  // TODO use inline style ?
  // TODO Macro
  // TODO Collect style strings
  // TODO ScalaCss Global Registry / Style Module

 

}



// uncaught exception: scala.MatchError: [Ljava.lang.String;@1 (of class [Ljava.lang.String;)