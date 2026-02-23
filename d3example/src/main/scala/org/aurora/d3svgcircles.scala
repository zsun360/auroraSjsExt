package org.aurora

import typings.d3.mod as d3Mod

import typings.std.global.{console, window}

import scala.scalajs.js
import org.scalajs.dom.{SVGCircleElement}
import scala.collection.immutable.LazyList.cons
import typings.d3Selection.mod.EnterElement
import org.scalajs.dom.Element
import scala.util.Random

import org.aurora.hldesign.StandardSVGView

object d3svgcircles extends StandardSVGView :

  override def rerender(): Unit = ???

  case class CircleData(id: Double,radius:Double, color: String, x: Double, y: Double)


  import js.JSConverters._
  val data = (1 to 551).map{i =>
    CircleData(i, 10 + Math.random() * 20, s"hsl(${Math.random() * 360}, 100%, 50%)", Math.random() * width, Math.random() * height)
  }.toSeq.toJSArray



  def start(): Unit = 
    console.info("Starting d3svg example")
    // val svg = d3Mod.select(s"#$svgcircles")
    //   .attr("width", width)
    //   .attr("height", height)
    //   .style("border", "1px solid black")

    import org.aurora.d3utils.*

    lazy val circles = svg.selectAll[SVGCircleElement, CircleData]("circle")
      .data(data)
      .join("circle" )
      .attr("cx", callback {(cd:CircleData) => cd.x })
      .attr("cy", callback {(cd:CircleData) => cd.y })
      .attr("fill", callback {(cd:CircleData) => cd.color })
      .attr("r", {(cd:CircleData) => cd.radius }.toCallback)  //alternative way to create the call back via extension method. not sure which way I like better


    // import typings.d3Selection.mod.ValueFn
    // import typings.d3Transition.mod.Transition_
    // type VFNDynamic[DATUM,R] = ValueFn[js.Dynamic, DATUM, R]

    // var tf = true
    def tfcolorf = Random.nextInt(8)  match 
      case 0 => "red"
      case 1 => "blue"
      case 2 => "green"
      case _ => "black"
    

    def f: VFnJSDynamic[Any, Unit] = 
     (thisArg:Any,d:Any,index:Double,data:Any)  => {

        d3Mod.active(thisArg.asInstanceOf[Element])
          .transition()
          .duration(1000) 
          .style("fill", tfcolorf)
          .asInstanceOf[TRANSITION]
          .on("end", f)
      }

    circles.asInstanceOf[TRANSITION]
      .transition()
      .duration(1500) 
      .style("fill", "black")
      .on("end", f) 
    
  


 



