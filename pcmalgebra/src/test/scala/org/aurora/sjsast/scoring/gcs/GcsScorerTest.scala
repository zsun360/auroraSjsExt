package org.aurora.sjsast.scoring.gcs

import org.aurora.sjsast.{IntValue, SingleValueUnit, StringValue}
import org.aurora.sjsast.scoring.ClinicalFacts
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GcsScorerTest extends AnyWordSpec with Matchers:

  "GcsScorer" should {
    "derive a total, severity, and source from complete component scores" in {
      val facts = clinicalFacts(
        "gcs_eye" -> intValue(2),
        "gcs_verbal" -> intValue(2),
        "gcs_motor" -> intValue(3)
      )

      GcsScorer.compute(facts).shouldBe(
        Some(
          GcsScorer.Result(
            total = Some(7),
            severity = Some(GcsSeverity.Severe),
            source = Some(GcsTotalSource.Derived),
            status = None
          )
        )
      )
    }

    "resolve text component inputs before deriving a total" in {
      val facts = clinicalFacts(
        "gcs_eye" -> textValue("to voice"),
        "gcs_verbal" -> textValue("confused"),
        "gcs_motor" -> textValue("withdraws")
      )

      GcsScorer.compute(facts).map(_.total).shouldBe(Some(Some(11)))
      GcsScorer.compute(facts).flatMap(_.severity).shouldBe(Some(GcsSeverity.Moderate))
    }

    "use a valid manual total when components are incomplete" in {
      val facts = clinicalFacts("gcs_total" -> intValue(14))

      GcsScorer.compute(facts).shouldBe(
        Some(
          GcsScorer.Result(
            total = Some(14),
            severity = Some(GcsSeverity.Mild),
            source = Some(GcsTotalSource.Manual),
            status = None
          )
        )
      )
    }

    "mark GCS as not testable when any component is not testable" in {
      val facts = clinicalFacts(
        "gcs_eye" -> intValue(2),
        "gcs_verbal" -> textValue("NT"),
        "gcs_motor" -> intValue(5),
        "gcs_total" -> intValue(7)
      )

      GcsScorer.compute(facts).shouldBe(
        Some(
          GcsScorer.Result(
            total = None,
            severity = None,
            source = None,
            status = Some(GcsStatus.NotTestable)
          )
        )
      )
    }

    "mark GCS as incomplete when partial component data cannot produce a total" in {
      val facts = clinicalFacts(
        "gcs_eye" -> intValue(4),
        "gcs_motor" -> intValue(5)
      )

      GcsScorer.compute(facts).shouldBe(
        Some(
          GcsScorer.Result(
            total = None,
            severity = None,
            source = None,
            status = Some(GcsStatus.Incomplete)
          )
        )
      )
    }

    "ignore absent GCS data" in {
      GcsScorer.compute(ClinicalFacts.Empty).shouldBe(None)
    }
  }

  private def clinicalFacts(entries: (String, SingleValueUnit)*): ClinicalFacts =
    ClinicalFacts(
      values = entries.map { case (key, value) => key -> List(value) }.toMap,
      issueNames = Set.empty
    )

  private def intValue(value: Int): SingleValueUnit =
    SingleValueUnit(IntValue(value), "_")

  private def textValue(value: String): SingleValueUnit =
    SingleValueUnit(StringValue(value), "_")
