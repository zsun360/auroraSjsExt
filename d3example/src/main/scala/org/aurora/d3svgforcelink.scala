package org.aurora

import typings.d3.mod as d3Mod

import typings.std.global.{console, window}

import scala.scalajs.js
import org.scalajs.dom.{SVGCircleElement, HTMLElement}
import typings.d3Scale.mod.NumberValue
import js.JSConverters.*
import typings.d3Axis.mod.{AxisScale,AxisDomain}
import typings.d3Selection.mod.Selection_
import typings.d3Axis.mod.Axis
import org.scalajs.dom.HTMLHtmlElement
import org.scalajs.dom.SVGGElement
import org.scalajs.dom.SVGSVGElement
import org.aurora.d3utils.*
import scala.util.Random
import org.scalajs.dom.Element
import typings.d3Shape.mod.Line_
import typings.d3Selection.mod.ArrayLike
import typings.d3Force.mod.Force
import com.raquo.airstream.ownership.ManualOwner
import org.aurora.hldesign.StandardSVGView
/**
 * Main notes:
  Watch how Select[?,?,?,?] changes with "builder" operations, like data()
*/
//note the module name will correspond to the id on the svg tag in index.html
object d3svgforcelink extends StandardSVGView :
  // lazy val svg = d3Mod.select(s"#${nameid}")
  //   .attr("width", width)
  //   .attr("height", height)
  //   .style("border", "1px solid black")
  //   .append("g")
  //   .attr("transform", s"translate(${0}, ${0})")

  lazy val node =  svg.append("g")
    .selectAll("circle")
    .data(jsnodes)  
    .enter()
    .append("circle")
    .attr("r", 10)
    .attr("id",idf((n:js.Dynamic) => n.id.toString()))
    .attr("fill","#69b3a2")

  def random = Random.nextInt(10)*width/10.0
  case class Node(id:String, x:Double = 0, y:Double = 0)
  case class Link(source:Node, target:Node)

  lazy val nm = "1234567890"  //node map from string key to Node
    .toCharArray()
    .map{c => c.toString -> Node(s"$c",random, random)}.toMap
  val nmKeys = nm.keySet.toSeq.toJSArray

  import js.Dynamic.literal
  def init() = nmKeys.map{k => nm(k)}.map{ n => literal{"id" -> n.id; "x" -> n.x; "y" -> n.y} }.toJSArray 
  var jsnodes = init()// nmKeys.map{k => nm(k)}.map{ n => literal{"id" -> n.id; "x" -> n.x; "y" -> n.y} }.toJSArray //convert case class to js.Dynamic to be in native java script data

  def f(  lambda: (d:js.Dynamic) => Double): VFnELEMENT[js.Dynamic,Double] = 
     (thisArg:Element,d:js.Dynamic,index:Double,data:Any)  => {
        lambda(d)
    }

  def idf(lambda: (d:js.Dynamic) => String): VFnELEMENT[js.Dynamic,String]  =
    (thisArg:Element,d:js.Dynamic,index:Double,data:Any)  => {
      lambda(d)
  }

  def ticked =  (thisArg:Any) => {
      node.asInstanceOf[Selection_[Element, js.Dynamic, Any, Any]]
        .attr("cx", f((d:js.Dynamic) => d.x.asInstanceOf[Double] ))
        .attr("cy", f((d:js.Dynamic) => d.y.asInstanceOf[Double] ))
      ()  
    }

  lazy val sim = d3Mod.forceSimulation(jsnodes)  //the nodes of the simulation are the values of the node map
    .force("charge",d3Mod.forceManyBody().strength(-50).asInstanceOf[Force[js.Object&js.Dynamic,Unit]])  //this is how you set the charge force, using a callback to convert the node data to a forceManyBody
    .force("center",d3Mod.forceCenter(width/2,height/2).asInstanceOf[Force[js.Object&js.Dynamic,Unit]])  //this is how you set the center force, using a callback to convert the node data to a forceCenter
    .on("tick", ticked )


  def start(): Unit = 
    console.info("Starting d3svgforcelink example")

    //event handling when text box changes
    nameVar.signal.foreach{ _ => 
      rerender()
    }  //restart the simulation when the nameVar changes, just to show how you can interact with the simulation from laminar. not sure if this is the best way to do it, but it works for demonstration purposes.
    
  def rerender(): Unit = 
    val newNodes = init() //create new random nodes
    node.data(  newNodes) 
    //    .enter()
    sim.nodes(newNodes)  //update the nodes of the simulation to trigger the simulation to update. not sure if this is the best way to do it, but it works for demonstration purposes.
    sim.alpha(0.5).restart() //update the data of the nodes to trigger the simulation to update. not sure if this is the best way to do it, but it works for demonstration purposes.






   
