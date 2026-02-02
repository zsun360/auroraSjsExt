package org.aurora.sjsast

import scala.scalajs.js
import typings.auroraLangium.distTypesSrcLanguageGeneratedAstMod as GenAst
import org.aurora.sjsast.RefCoordinate.ClinicalCoordinateValue

case class NGC(
    name: String,
    narratives: LHSet[NL_STATEMENT] = LHSet(),
    coordinates: LHSet[RefCoordinate] = LHSet(),
    refs: LHSet[QuReferences] = LHSet()
)

object NGC:
  def apply(ngc: GenAst.NamedGroupClinical): NGC =
    val narratives = LHSet(ngc.narrative.toList.map(NL_STATEMENT.apply)*)

    val coords = LHSet(
      ngc.coord.toList.map { (x: ClinicalCoordinateValue) =>
        val ast = x.asInstanceOf[js.Dynamic]
        ast.`$type`.asInstanceOf[String] match {
          case "ClinicalCoordinate" => 
            ClinicalCoordinate(x.asInstanceOf[GenAst.ClinicalCoordinate])
          case "ClinicalValue" => 
            ClinicalValue(x.asInstanceOf[GenAst.ClinicalValue])
          case _ => 
            throw new Exception(s"Unsupported coordinate type in NGC")
        }
      }*
    )

    val refs = ngc.qurc.toOption match {
      case Some(qrs) => LHSet(QuReferences(qrs))
      case None      => LHSet()
    }

    NGC(
      name = ngc.name,
      narratives = narratives,
      coordinates = coords,
      refs = refs
    )