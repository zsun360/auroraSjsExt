package org.aurora.hldesign


import org.aurora.HelloWorld
import com.raquo.airstream.ownership.ManualOwner
import com.raquo.laminar.api.L.{*, given}
import typings.d3.mod as d3Mod

trait StandardSVGView :
  private object utils:
    extension(o:Any)
      def cleanname =
        val sn = o.getClass().getSimpleName()
        sn.indexOf("$") match
          case -1 => sn
          case i => sn.substring(0, i)

  lazy val height= 400 // standardize size here
  lazy val width = 400

  given Owner = ManualOwner() //owner needed for laminar foreach side effects
  val nameVar = HelloWorld.nameVar   //standardize access to Laminar based  event handling from form events

  //access to module name that corresponds to the id property within svg tag in index.html
  lazy val nameid = 
    import utils.*
    this.cleanname
  
  //note the module name that corresponds to the id property within svg tag in index.html
  def svg = d3Mod.select(s"#${nameid}")
    .append("g")
    .attr("width", width)
    .attr("height", height)
    .style("border", "1px solid black") //This doesn't do anything
    .attr("transform", s"translate(${0}, ${0})")

  def start(): Unit  //standardize starting
  def rerender(): Unit  //standaredize rerendering

