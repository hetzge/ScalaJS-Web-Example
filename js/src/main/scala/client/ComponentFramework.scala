package client

import org.scalajs.jquery._

import scalacss.DevDefaults._
import scalacss.ScalatagsCss._
import scalacss.mutable.Register
import scalacss.{NonEmptyVector, UnicodeRange}
import scalatags.jsdom.Frag
import org.scalajs.dom.raw.Node

trait HtmlElement extends Frag {
  /**
    * The last rendered html.
    */
  final var renderedHtml: Node = null

  /**
    * This function produce the html to render.
    */
  def html: Frag

  final def render = {
    renderedHtml = html.render
    afterRender
    renderedHtml
  }

  def isRendered = renderedHtml != null

  /**
    * This function can be overriden if you need to do things after the element is rendered to dom.
    */
  def afterRender = {}

  /**
    * Shortcut jquery access on this element.
    */
  def $ = {
    if (!isRendered) {
      throw new IllegalAccessError("jQuery can only be accessed on already rendered elements")
    }

    jQuery(renderedHtml)
  }
}

trait CssStyle extends StyleSheet.Inline {
  App.styles += this
}

trait ReactiveElement extends HtmlElement {
  def reactOn[T](model: Model[T]) = {
    model.watch { t: T =>
      $.replaceWith(this.render)
    }
  }
}

case class ReactiveText(model: Model[_]) extends ReactiveElement {

  import scalatags.JsDom.all._

  override def html = span(model.get().toString())

  reactOn(model)
}

abstract class ReactiveListElement[T](model: Model[Seq[T]]) extends ReactiveElement {
  def listHtml(value: T): Frag

  import scalatags.JsDom.all._

  override def html = div(model.get().map(listHtml(_)): _*)

  reactOn(model)
}

case class ReactiveInput(model: Model[String]) extends ReactiveElement {

  import scalatags.JsDom.all._

  override def html = input(value := model.get())

  /**
    * Temporal hack to prevent self calling change events
    */
  var ignoreValue = ""

  override def afterRender = {
    model.watch { t: String =>
      if (!t.equals(ignoreValue)) {
        ignoreValue = t
        $.value(t)
      }
    }

    $.change { t: Any =>
      if (!t.equals(ignoreValue)) {
        val newValue = $.value().toString
        ignoreValue = newValue
        model(newValue)
      }
    }
  }

}