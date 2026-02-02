package org.aurora.sjsast
 
import scala.scalajs.js
import typings.auroraLangium.distTypesSrcLanguageGeneratedAstMod as GenAstMod

trait RefCoordinate{
    def name: String
}

object RefCoordinate:
  type ClinicalCoordinateValue = GenAstMod.ClinicalCoordinate | GenAstMod.ClinicalValue

  def apply(ast: GenAstMod.ReferenceCoordinate | GenAstMod.ClinicalValue): RefCoordinate =
    val node = ast.asInstanceOf[js.Dynamic]
    node.`$type`.asInstanceOf[String] match {
      case "ClinicalCoordinate" => 
        ClinicalCoordinate(ast.asInstanceOf[GenAstMod.ClinicalCoordinate])
      case "IssueCoordinate"    => 
        IssueCoordinate(ast.asInstanceOf[GenAstMod.IssueCoordinate])
      case "OrderCoordinate"    => 
        OrderCoordinate(ast.asInstanceOf[GenAstMod.OrderCoordinate])
      case "ClinicalValue"      => 
        ClinicalValue(ast.asInstanceOf[GenAstMod.ClinicalValue])
      case unknown => 
        throw new Exception(s"Unsupported coordinate type: $unknown")
    }