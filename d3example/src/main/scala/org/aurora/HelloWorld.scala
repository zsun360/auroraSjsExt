package org.aurora

import com.raquo.laminar.api.L.{*, given}
import typings.std.global.{console, document, window}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
object HelloWorld {

  @JSImport("@find/**/HelloWorld.less", JSImport.Namespace)
  @js.native private object Stylesheet extends js.Object

  val _ = Stylesheet // Use import to prevent DCE
  lazy val nameVar = Var(initial = "world")

  def apply(): HtmlElement = {
    div(
      cls("HelloWorld"),
      label("Your names: "),
      input(
        onMountFocus,
        placeholder := "Enter your name here",
        onInput.mapToValue --> nameVar
      ),
      div(
        cls("-greeting"),
        "Hello there: ",
        text <-- nameVar.signal.map(_.toUpperCase),
      )
    )
  }
}
