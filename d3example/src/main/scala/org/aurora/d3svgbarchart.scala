package org.aurora

import typings.d3.mod as d3Mod

import typings.std.global.{console, window}

import scala.scalajs.js
import js.JSConverters.*
import typings.d3Axis.mod.{AxisScale,AxisDomain}
import typings.d3Selection.mod.Selection_
import typings.d3Axis.mod.Axis
/**
 * Main notes:
  Watch how Select[?,?,?,?] changes with "builder" operations, like data()
*/
import org.aurora.hldesign.StandardSVGView

object d3svgbarchart extends StandardSVGView :

  override def rerender(): Unit = ???

  val data = (1 to 300).map{_.toDouble}.toJSArray
  val datay = (1 to 300).map{_.toDouble * 2}.toJSArray

  def start(): Unit = 
    console.info(s"Starting $nameid example")
  
    lazy val xScale = d3Mod.scaleLinear()
      .domain(js.Array(data.min,data.max))
      .range(js.Array(0, width).map{_.toDouble})
    lazy val yScale = d3Mod.scaleLinear()
      .domain(js.Array(datay.min,datay.max))
      .range(js.Array(height, 0).map{_.toDouble})  


    import org.aurora.d3utils.*
    val xAxis = d3Mod.axisTop(xScale.toAxisScale)//.ticks(25)
     val yAxis = d3Mod.axisRight(yScale.toAxisScale)//.ticks(15)

    svg
     .append("rect")
     .attr("width", width)
     .attr("height", height)
     .style("fill", "pink")
    svg.append("g")
      .data(data)  //note the type changes to SELECTION_[?,?,?,?].  without this call, the type is SELECTION_[?,Nothing,?,?,?]
      .transform(0,height)
      .callAxis(xAxis)
      .asInstanceOf[TRANSITION]
        .transition()
        .duration(2000)
        .style("color","red")

    svg.append("g")
      .data(data)  //note the type changes to SELECTION_[?,?,?,?].  without this call, the type is SELECTION_[?,Nothing,?,?,?]
       .transform(0,0)
      .callAxis(yAxis)
      .asInstanceOf[TRANSITION]
        .transition()
        .duration(2000)
        .style("color","red")
    


    import typings.d3Selection.mod.ValueFn
    type F = ValueFn[Any,Any,Unit]
    val f: F = (thisArg:Any,x:Any,elem:Any,data:Any)  => console.info("Axis animation complete"); 



    svg.append("text")
      .attr("x", width/2)
      .attr("y", height/2)
      .attr("text-anchor", "middle")
      .attr("font-size", "16px")
      .text("D3 Bar Chart Work in Progress Example")
      .asInstanceOf[TRANSITION]
      .transition()
        .duration(2000)
        .style("fill","blue")






