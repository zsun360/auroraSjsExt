package org.aurora.sjsast.scoring.gcs

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GcsEnumTest extends AnyWordSpec with Matchers:

  "GcsEnum" should {
    "carry scores and descriptions for each GCS component" in {
      Eye.Spontaneous.score shouldBe 4
      Eye.Spontaneous.description shouldBe "eyes open spontaneously"

      Verbal.Oriented.score shouldBe 5
      Verbal.Oriented.description shouldBe "oriented verbal response"

      Motor.ObeysCommands.score shouldBe 6
      Motor.ObeysCommands.description shouldBe "obeys commands"
    }

    "represent component resolution as a typed state" in {
      val resolved: ComponentResolution[Eye] =
        ComponentResolution.Resolved(Eye.ToVoice)

      resolved shouldBe ComponentResolution.Resolved(Eye.ToVoice)
      ComponentResolution.Missing shouldBe ComponentResolution.Missing
      ComponentResolution.NotTestable shouldBe ComponentResolution.NotTestable
    }

    "carry stable output values for GCS outputs" in {
      GcsSeverity.Severe.outputValue shouldBe "severe"
      GcsStatus.NotTestable.outputValue shouldBe "not_testable"
      GcsTotalSource.Derived.outputValue shouldBe "derived"
    }

    "find Eye condition by score" in {
      Eye.findByScore(2) should be (Some(Eye.ToPain))
      Eye.findByScore(10) should be (None)
    }

    "find Verbal condition by score" in {
      Verbal.findByScore(3) should be (Some(Verbal.Words))
      Verbal.findByScore(7) should be (None)
    }

    "find Motor condition by score" in {
      Motor.findByScore(4) should be (Some(Motor.WithdrawsFromPain))
      Motor.findByScore(10) should be (None)
    }


  }

