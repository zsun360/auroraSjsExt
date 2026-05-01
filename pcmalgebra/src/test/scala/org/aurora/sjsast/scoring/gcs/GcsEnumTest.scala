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
  }

