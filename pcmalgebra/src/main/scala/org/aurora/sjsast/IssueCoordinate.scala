package org.aurora.sjsast

import scala.scalajs.js

case class IssueCoordinate(
    name: String,
    fromMods: List[String] = List(),
    narratives: LHSet[NL_STATEMENT] = LHSet(),
    qurefs: LHSet[QuReferences] = LHSet(),
    qu: QU = QU()
) extends RefCoordinate

object IssueCoordinate:
    def apply(ic: GenAst.IssueCoordinate): IssueCoordinate = 
        val name = ic.asInstanceOf[js.Dynamic].selectDynamic("name").toString
        val mods = ic.mods.toList.flatMap { m =>
        val refText = m.asInstanceOf[js.Dynamic].selectDynamic("$refText")
        if (refText != js.undefined) Some(refText.asInstanceOf[String]) else None
        }
        val narratives = LHSet(ic.narrative.toList.map(NL_STATEMENT(_))*)
        val qurefs = LHSet(ic.qurc.toList.map(QuReferences(_))*)
        val qu = QU(ic.qu)
        IssueCoordinate(name, mods, narratives, qurefs, qu)