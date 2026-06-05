package org.aurora.sjsast.scoring.gcs

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GcsEnumTest extends AnyWordSpec with Matchers:

  "GcsEnum" should {
    "carry scores and descriptions for each GCS component" in {
      Eye.Spontaneous.score.shouldBe(4)
      Eye.Spontaneous.description.shouldBe("eyes open spontaneously")

      Verbal.Oriented.score.shouldBe(5)
      Verbal.Oriented.description.shouldBe("oriented verbal response")

      Motor.ObeysCommands.score.shouldBe(6)
      Motor.ObeysCommands.description.shouldBe("obeys commands")
    }

    "represent component resolution as a typed state" in {
      val resolved: ComponentResolution[Eye] =
        ComponentResolution.Resolved(Eye.ToVoice)

      resolved.shouldBe(ComponentResolution.Resolved(Eye.ToVoice))
      ComponentResolution.Missing.shouldBe(ComponentResolution.Missing)
      ComponentResolution.NotTestable.shouldBe(ComponentResolution.NotTestable)
    }

    "carry stable output values for GCS outputs" in {
      GcsSeverity.Severe.outputValue.shouldBe("severe")
      GcsStatus.NotTestable.outputValue.shouldBe("not_testable")
      GcsTotalSource.Derived.outputValue.shouldBe("derived")
    }

    "find Eye condition by score" in {
      Eye.findByScore(2).shouldBe(Some(Eye.ToPain))
      Eye.findByScore(10).shouldBe(None)
    }

    "parse Eye component inputs" in {
      Eye.parse("spontaneous").shouldBe(Some(Eye.Spontaneous))
      Eye.parse("to voice").shouldBe(Some(Eye.ToVoice))
      Eye.parse("TO_PAIN").shouldBe(Some(Eye.ToPain))
      Eye.parse("no-eye-opening").shouldBe(Some(Eye.None))
      Eye.parse("unknown").shouldBe(None)
    }

    "find Verbal condition by score" in {
      Verbal.findByScore(3).shouldBe(Some(Verbal.Words))
      Verbal.findByScore(7).shouldBe(None)
    }

    "find Motor condition by score" in {
      Motor.findByScore(4).shouldBe(Some(Motor.WithdrawsFromPain))
      Motor.findByScore(10).shouldBe(None)
    }

    "derive severity from a valid GCS total" in {
      GcsSeverity.fromTotal(3).shouldBe(Some(GcsSeverity.Severe))
      GcsSeverity.fromTotal(8).shouldBe(Some(GcsSeverity.Severe))
      GcsSeverity.fromTotal(9).shouldBe(Some(GcsSeverity.Moderate))
      GcsSeverity.fromTotal(12).shouldBe(Some(GcsSeverity.Moderate))
      GcsSeverity.fromTotal(13).shouldBe(Some(GcsSeverity.Mild))
      GcsSeverity.fromTotal(15).shouldBe(Some(GcsSeverity.Mild))
    }

    "reject invalid GCS totals when deriving severity" in {
      GcsSeverity.fromTotal(2).shouldBe(None)
      GcsSeverity.fromTotal(16).shouldBe(None)
    }

    "parse GCS severity output values" in {
      GcsSeverity.fromOutputValue("severe").shouldBe(Some(GcsSeverity.Severe))
      GcsSeverity.fromOutputValue("MODERATE").shouldBe(Some(GcsSeverity.Moderate))
      GcsSeverity.fromOutputValue("mild").shouldBe(Some(GcsSeverity.Mild))
      GcsSeverity.fromOutputValue("unknown").shouldBe(None)
    }

    "parse GCS status output values" in {
      GcsStatus.fromOutputValue("not_testable").shouldBe(Some(GcsStatus.NotTestable))
      GcsStatus.fromOutputValue("not testable").shouldBe(Some(GcsStatus.NotTestable))
      GcsStatus.fromOutputValue("INCOMPLETE").shouldBe(Some(GcsStatus.Incomplete))
      GcsStatus.fromOutputValue("unknown").shouldBe(None)
    }

    "parse GCS total source output values" in {
      GcsTotalSource.fromOutputValue("derived").shouldBe(Some(GcsTotalSource.Derived))
      GcsTotalSource.fromOutputValue("MANUAL").shouldBe(Some(GcsTotalSource.Manual))
      GcsTotalSource.fromOutputValue("unknown").shouldBe(None)
    }

  }
