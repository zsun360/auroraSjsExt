package org.aurora

import org.scalajs.dom.{SVGElement,SVGGElement,SVGSVGElement,SVGCircleElement}
import scala.scalajs.js
import typings.d3Selection.mod.{ValueFn,ArrayLike}
import org.scalajs.dom.{HTMLHtmlElement,HTMLElement,Element}
import typings.d3Scale.mod.{ScaleLinear_,NumberValue}
import typings.d3Axis.mod.{AxisScale,AxisDomain,Axis}
import typings.d3Axis.mod.Axis
import typings.d3Transition.mod.Transition_
import typings.d3Selection.mod.Selection_

import typings.d3Shape.mod.Line_

package object d3utils:
  val temp = "x"
  type SELECTIONTYPE[DATUM] = Selection_[SVGGElement|SVGSVGElement, DATUM, HTMLElement, Any]
  type SELECTIONANY = Selection_[SVGGElement|SVGSVGElement, Any,Any,Any]
  type TRANSITION = Transition_[js.Dynamic, Any, Any, Any]

    //TODO: STANDARDIZE ValueFn across examples
  type VFnRETURN = Null | String | Double | Boolean | (js.Array[String | Double]) //broad possible return types are common
  type VFnELEMENT[DATUM,R] = ValueFn[Element, DATUM, R]
  type VFnDATUM[DATUM] = ValueFn[Element, DATUM, VFnRETURN]
  type VFnJSDynamic[DATUM,R] = ValueFn[js.Dynamic, DATUM, R]//js.Function3[js.Dynamic, DATUM, Double, R]

  val exampleLambda: VFnDATUM[js.Dynamic] = (thisArg:Element,d:js.Dynamic,index:Double,data:Any)  => {
    d.id.toString
  }

  extension [DATUM](s:Selection_[SVGGElement|SVGSVGElement,DATUM,HTMLElement,Any])  
    def applyAxis(axis:Axis[DATUM]): Unit = 
      axis.apply(s.asInstanceOf[SELECTIONANY])

    def callAxis(axis:Axis[DATUM]): Selection_[SVGGElement|SVGSVGElement,DATUM,HTMLElement,Any] = 
      s.call( { (sel: SELECTIONTYPE[DATUM], a: Axis[DATUM]) => 
        a.apply(sel.asInstanceOf[SELECTIONANY]) }.asInstanceOf[scala.scalajs.js.Function2[Any, Any, Unit]]//this is the logic to draw the axis
       , axis)

    def transform(x:Int,y:Int)  = s.attr("transform", s"translate($x, $y)")


  extension [T] (s:ScaleLinear_[T,Nothing,Nothing])  
    def toAxisScale: AxisScale[T] = s.asInstanceOf[AxisScale[T]]

