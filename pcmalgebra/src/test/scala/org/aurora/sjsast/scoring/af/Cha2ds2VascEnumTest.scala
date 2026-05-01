package org.aurora.sjsast.scoring.af

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Cha2ds2VascEnumTest extends AnyWordSpec with Matchers:

  "Cha2ds2VascEnum" should {
    "carry points and descriptions for sex category" in {
      SexCategory.Female.points shouldBe 1
      SexCategory.Female.description shouldBe "female sex category"

      SexCategory.NotFemale.points shouldBe 0
      SexCategory.NotFemale.description shouldBe "not female sex category"
    }

    "carry points and descriptions for risk factors" in {
      Cha2ds2VascRiskFactor.CongestiveHeartFailure.points shouldBe 1
      Cha2ds2VascRiskFactor.Age75OrOlder.points shouldBe 2
      Cha2ds2VascRiskFactor.PriorStrokeTiaThromboembolism.points shouldBe 2
      Cha2ds2VascRiskFactor.Age65To74.description shouldBe "age 65 to 74 years"
    }

    "carry stable output values for risk bands and status" in {
      Cha2ds2VascRiskBand.Low.outputValue shouldBe "low"
      Cha2ds2VascRiskBand.Intermediate.outputValue shouldBe "intermediate"
      Cha2ds2VascRiskBand.High.outputValue shouldBe "high"
      Cha2ds2VascStatus.InsufficientData.outputValue shouldBe "insufficient_data"
    }
  }

