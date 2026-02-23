package org.aurora

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@find/**/HelloWorld.less", JSImport.Namespace)
@js.native private object Stylesheet extends js.Object

val _ = Stylesheet // force initialization to prevent DCE (Dead Code Elimination) from removing the stylesheet

@main def main(): Unit = {

  //draws animated rotating globe and random lines around it 
  d3canvassphere.start

  //draws circles in svg
  d3svgcircles.start()

  //
  d3svgpath.start()
  d3svgaxis.start()
  d3svgforcelink.start()
  d3svgbarchart.start()



  //when dom is loaded creates basic form
  renderOnDomContentLoaded(
    container = dom.document.querySelector("#app"),
    rootNode = {
      div(
        cls("Main"),
        h1("Laminar Template (scroll down to see d3 examples)"),
        HelloWorld(),
      )
    }
  )
}
