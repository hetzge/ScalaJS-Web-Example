package client

import scalacss.DevDefaults._
import scalacss.ScalatagsCss._
import scalacss.ext.CssReset
import scalacss.mutable.Register
import scalacss.{NonEmptyVector, UnicodeRange}
import scalatags.jsdom.Frag
import org.scalajs.jquery._
import shared._
import scalajs.concurrent.JSExecutionContext.Implicits.queue


object Component {


  object GlobalStyle extends CssStyle {
    import dsl._

    val reset = style(CssReset.meyer)
  }

  object ThemeStyle extends CssStyle {

    import dsl._

    val primaryColor = red
    val secondaryColor = blue

    val primaryFontColor = Color("#fff111")

    var default = style(
      unsafeRoot("*")(
        color(primaryFontColor),
        fontFamily :=! "Roboto, sans-serif"
      ))
  }

  object AtomStyle extends CssStyle {
    import dsl._

    val headline1 = style(
      fontSize(300 %%),
      fontWeight._800
    )

    val headline2 = style(
      fontSize(250 %%),
      fontWeight._800
    )

    val headline3 = style(
      fontSize(200 %%),
      fontWeight._700
    )

    val headline4 = style(
      fontSize(150 %%),
      fontWeight._700
    )

    val headline5 = style(
      fontSize(125 %%),
      fontWeight._600
    )

    val h1 = style(
      unsafeRoot("h1")(headline1)
    )
  }

  object TitledPaneStyle extends CssStyle {
    import dsl._

    val button = style(
      fontSize(300 %%),
      margin(12 px))
  }

  case class UserMolecul(userModel: Model[User]) extends ReactiveElement{



  }

  case class DemoElement() extends ReactiveElement {
    val css = TitledPaneStyle

    override def html = Html.main

    GlobalStyle
    ThemeStyle

    object Html {
      import scalatags.JsDom.all._

      import autowire._ // !!!
      MyClient[AutowireApi].getAllVideos(ApiRequest(Seq(VideoField.USERNAME))).call().map{ (result: ApiResult) =>
        println("Hello world")
        for(e <- result.entities){
          //          println(e.get(VideoField.USERNAME))
        }
      }

      val main = div(
        div(AtomStyle.headline1, "Headline 1"),
        div(AtomStyle.headline2, "Headline 2"),
        div(AtomStyle.headline3, "Headline 3"),
        div(AtomStyle.headline4, "Headline 4"),
        div(AtomStyle.headline5, "Headline 5"),
        h1("Headline 1"),
        div(h1("A other headline"), div("Content"))
      )
    }


  }


}