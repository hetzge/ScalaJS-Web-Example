package client

import scalacss.DevDefaults._
import scalacss.ScalatagsCss._
import scalacss.mutable.Register
import scalacss.{ NonEmptyVector, UnicodeRange }
import scalatags.jsdom.Frag
import org.scalajs.jquery._
import shared._
import scalajs.concurrent.JSExecutionContext.Implicits.queue


object Component{

  object TitledPaneStyle extends CssStyle{
    import dsl._

    val button = style(
      fontSize(200 %%),
      margin(12 px))
  }
  case class TitledPane(headline: Frag, text: String, x: Model[Int]) extends ReactiveElement {
    val css = TitledPaneStyle
    override def html = Html.main

    object Html{
      import scalatags.JsDom.all._

      val y = Model[String]("Text")
      val textDiv = div(headline, div(text, css.button)).render
      val clickDiv = div("Click me", onclick := reset _).render
      val main = div(textDiv, clickDiv, ReactiveText(y), ReactiveInput(y)).render
    }

    def reset(): Unit = {

      x((Math.random() * 100).toInt)

      import autowire._ // !!!
      MyClient[AutowireApi].getAllVideos(ApiRequest(Seq(VideoField.USERNAME))).call().map{ (result: ApiResult) =>
        println("Hello world")
        for(e <- result.entities){
          //          println(e.get(VideoField.USERNAME))
        }
      }

      println("blaaaaaaaggggg")


    }

  }


}