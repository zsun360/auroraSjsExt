package org.aurora.sjsast.scoring.af

import org.aurora.sjsast.{IntValue, SingleValueUnit, StringValue}
import org.aurora.sjsast.scoring.ClinicalFacts
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AfScorerTest extends AnyWordSpec with Matchers:

  "AfScorer" should {
    "derive CHA2DS2-VASc total and high risk band from complete typed facts" in {
      val facts = clinicalFacts(
        values = Map(
          "age" -> List(intValue(76)),
          "sex" -> List(textValue("female")),
          "cha2ds2_vasc_diabetes" -> List(textValue("absent")),
          "prior_stroke_tia_te" -> List(textValue("absent")),
          "vascular_disease" -> List(textValue("absent"))
        ),
        issueNames = Set("atrial_fibrillation", "heart_failure", "hypertension")
      )

      AfScorer.compute(facts).shouldBe(
        Some(
          AfScorer.Result(
            total = Some(5),
            riskBand = Some(Cha2ds2VascRiskBand.High),
            status = None
          )
        )
      )
    }

    "derive intermediate risk for a non-female patient with one point" in {
      val facts = clinicalFacts(
        values = Map(
          "age" -> List(intValue(52)),
          "sex" -> List(textValue("male")),
          "cha2ds2_vasc_heart_failure" -> List(textValue("absent")),
          "cha2ds2_vasc_diabetes" -> List(textValue("absent")),
          "prior_stroke_tia_te" -> List(textValue("absent")),
          "vascular_disease" -> List(textValue("absent"))
        ),
        issueNames = Set("atrial_fibrillation", "hypertension")
      )

      AfScorer.compute(facts).shouldBe(
        Some(
          AfScorer.Result(
            total = Some(1),
            riskBand = Some(Cha2ds2VascRiskBand.Intermediate),
            status = None
          )
        )
      )
    }

    "report insufficient data when atrial fibrillation is present but required factors are missing" in {
      val facts = clinicalFacts(
        values = Map(
          "age" -> List(intValue(70)),
          "sex" -> List(textValue("female"))
        ),
        issueNames = Set("atrial_fibrillation")
      )

      AfScorer.compute(facts).shouldBe(
        Some(
          AfScorer.Result(
            total = None,
            riskBand = None,
            status = Some(Cha2ds2VascStatus.InsufficientData)
          )
        )
      )
    }

    "ignore facts when atrial fibrillation is absent" in {
      val facts = clinicalFacts(
        values = Map(
          "atrial_fibrillation" -> List(textValue("absent")),
          "age" -> List(intValue(76)),
          "sex" -> List(textValue("female"))
        ),
        issueNames = Set.empty
      )

      AfScorer.compute(facts).shouldBe(None)
    }
  }

  private def clinicalFacts(
      values: Map[String, List[SingleValueUnit]],
      issueNames: Set[String]
  ): ClinicalFacts =
    ClinicalFacts(values = values, issueNames = issueNames)

  private def intValue(value: Int): SingleValueUnit =
    SingleValueUnit(IntValue(value), "_")

  private def textValue(value: String): SingleValueUnit =
    SingleValueUnit(StringValue(value), "_")
