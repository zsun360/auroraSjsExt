package org.aurora
import org.scalajs.dom

import scala.scalajs.js

object CanvasContext {
  type Ctx2D = dom.CanvasRenderingContext2D

  def context(id: String): Ctx2D = {
    val canvas = dom.document.getElementById(id).asInstanceOf[dom.html.Canvas]
    canvas.getContext("2d").asInstanceOf[Ctx2D]
  }
}