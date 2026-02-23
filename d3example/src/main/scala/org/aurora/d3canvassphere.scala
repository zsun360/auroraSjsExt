package org.aurora

import typings.d3.mod as d3Mod
import typings.d3Geo.mod.{GeoContext, GeoPermissibleObjects, GeoProjection_, GeoPath_}
import typings.geojson.geojsonStrings
import typings.geojson.mod.{LineString, Position}

import org.scalajs.dom.{document}
import typings.std.global.{console, document, window}
import typings.std.{ FrameRequestCallback, HTMLCanvasElement, stdStrings}


import org.scalajs.dom.{CanvasRenderingContext2D,Console}

import scala.scalajs.js
import scala.scalajs.js.|


object d3canvassphere:
    // not used...
    // where is my `?.` :( :(
  extension [T](ot: T | Null)  //not being used at this time but may be a useful idea in future
    def andThen[U](f: T => U | Null): U | Null =

      if ot != null then f(ot.asInstanceOf[T]) else null // todo: revisit with explicit nulls
  // this conforms structurally
  def isGeoContext(ctx: CanvasRenderingContext2D): GeoContext =
    ctx.asInstanceOf[GeoContext]

    



  def start(using context: CanvasRenderingContext2D): Double =
    context.lineWidth = 0.4
    context.strokeStyle = "rgba(255, 255, 255, 0.6)"

    val width  = 400//window.innerWidth
    val height = 400//window.innerHeight
    val size   = width min height

    d3Mod
      .select("#d3canvas")
      .attr("width", s"${width}px")
      .attr("height", s"${height}px")
      .style("border", "1px solid black")

    val projection: GeoProjection_ =
      d3Mod
        .geoOrthographic()
        .scale(0.45 * size)
        .translate(js.Tuple2(0.5 * width, 0.5 * height))

    val geoGenerator: GeoPath_[Any, GeoPermissibleObjects] =
      d3Mod.geoPath(projection, isGeoContext(context))

    val geometry = LineString(coordinates = js.Array[Position]())

    def rndLon = -180 + Math.random() * 360
    def rndLat = -90 + Math.random() * 180

    def addPoint(): Unit =
      geometry.coordinates.push(js.Array(rndLon, rndLat))

    def update: FrameRequestCallback =
      (time: Double) =>

        if geometry.coordinates.length < 6000 then addPoint()

        projection.rotate(js.Tuple2(time / 100, 1.0))

        context.clearRect(0, 0, width, height)
        context.beginPath()

        geoGenerator(geometry, null.asInstanceOf[js.Any])
        context.stroke()
        context.fillStyle = "rgba(55, 255, 255, 0.6)"
        context.fillRect(0, 0, 15, 15)

        window.requestAnimationFrame(update)

    window.requestAnimationFrame(update)
  end start
