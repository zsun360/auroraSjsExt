package org.aurora

import typings.d3.mod as d3Mod

import typings.std.global.{console, window}

import scala.scalajs.js
import org.scalajs.dom.{SVGCircleElement, HTMLElement}
import typings.d3Scale.mod.NumberValue
import js.JSConverters.*
import typings.d3Selection.mod.Selection_
import scala.util.Random
import org.scalajs.dom.Element
import typings.d3Shape.mod.Line_
import org.aurora.hldesign.StandardSVGView
/**
 * Main notes:
  Watch how Select[?,?,?,?] changes with "builder" operations, like data()
*/

object d3svgpath extends StandardSVGView :
  import org.aurora.d3utils.*

  // val width = 400
  // val height = 400



  override def rerender(): Unit = ???

  def start(): Unit = 
    console.info("Starting d3svgpath example")


    case class Node(id:String, x:Double = 0, y:Double = 0)
    case class Link(source:Node, target:Node)
    def random = Random.nextInt(10)*width/10.0
    val nm = "ABCDEFGHIJKLMNOPQRESTUVWXYZ"
      .toCharArray()
      .map{c => c.toString -> Node(s"$c",random, random)}.toMap
    val nmKeys = nm.keySet.toSeq.toJSArray

    // val svgG = d3Mod.select(s"#${svgpath}")
    //   .attr("width", width)
    //   .attr("height", height)
    //   .style("border", "1px solid black")
    //   .append("g")
    //   .attr("transform", s"translate(${0}, ${0})")


    val lineGenerator = d3Mod.line[Node]()
      .x( (n:Node,elem:Any, data:Any) => {console.info(s"(x,y) = (${n.x}, ${n.y})") ;  n.x })
      .y( (n:Node,elem:Any, data:Any) => n.y )

   
    val path = svg
      .append("path")  
      .datum(nmKeys.map{(d => nm(d))})
      .attr("d",callback((d:js.Array[Node]) => lineGenerator.apply(d))) //alternative way to create the call back via extension method. not sure which way I like better
      .attr("fill", "none") // Do not fill the path
      .attr("stroke", "red") // Set stroke color
      .attr("stroke-width", 3)
  

    val nodeGroups = svg.
       selectAll[SVGCircleElement, String]("circle")
        .data(nmKeys)
        .join("g")

    //each node group will contain a circle and text element
    nodeGroups
      .append("circle")
      .attr("cx", callback((d:String) =>nm(d).x))
      .attr("cy", callback((d:String) =>nm(d).y))
      .attr("r", 10)
      .attr("fill", "steelblue")
      .asInstanceOf[TRANSITION]
      .transition()
      .duration(2000) 
        .style("fill", "pink")
        .on("end", transitionLambda)


    nodeGroups
      .append("text")
        .attr("text-anchor", "middle")
        .attr("x", callback((d:String) =>nm(d).x))
        .attr("y", callback((d:String) =>nm(d).y))
        .attr("dy", ".35em") //this is to center the text vertically in the circle
        .attr("stroke", "black")
        .attr("fill", "yellow")
        .text(callback((d:String) =>{d })) //this is how you set the text of the circle to the node id, using a callback to convert the data to text


    import typings.d3Selection.mod.ValueFn
    import typings.d3Transition.mod.Transition_
    type TRANSITION = Transition_[js.Dynamic, Any, Any, Any]


    def transitionLambda: VFnJSDynamic[Any,Unit] =
     (thisArg:js.Dynamic,d:Any,index:Double,data:Any)  => {

        d3Mod.active(thisArg.asInstanceOf[Element])
          .transition()
          .duration(1000) 
          .style("fill", "white")
          .asInstanceOf[TRANSITION]
          // .on("end", f)
      }      


   
