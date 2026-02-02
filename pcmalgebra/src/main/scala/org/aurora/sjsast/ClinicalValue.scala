package org.aurora.sjsast

case class ClinicalValue(
    name: String, 
    values: List[SingleValueUnit] = List(), // Added to hold numeric data
    narrative: LHSet[NL_STATEMENT] = LHSet(), 
    qurefs: LHSet[QuReferences] = LHSet()
) extends RefCoordinate

object ClinicalValue:
    def apply(ast: GenAst.ClinicalValue): ClinicalValue =
        ClinicalValue(
            name = ast.name,
            // Convert js.Array to List and map using the SingleValueUnit companion
            values = ast.values.toList.map(SingleValueUnit.apply),
            
            // Convert narrative using NL_STATEMENT IR (assuming its apply exists)
            narrative = LHSet(ast.narrative.toList.map(NL_STATEMENT.apply)*),
            
            // Handle the optional QuReferences
            qurefs = ast.qurc.toOption
                .map(q => LHSet(QuReferences(q)))
                .getOrElse(LHSet())
        )