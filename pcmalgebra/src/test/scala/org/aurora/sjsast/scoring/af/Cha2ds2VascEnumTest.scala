package org.aurora.sjsast.scoring.af

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Cha2ds2VascEnumTest extends AnyWordSpec with Matchers:

  "Cha2ds2VascEnum" should {
    "carry points and descriptions for sex category" in {
      SexCategory.Female.points.shouldBe(1)
      SexCategory.Female.description.shouldBe("female sex category")

      SexCategory.NotFemale.points.shouldBe(0)
      SexCategory.NotFemale.description.shouldBe("not female sex category")
    }

    "carry points and descriptions for risk factors" in {
      Cha2ds2VascRiskFactor.CongestiveHeartFailure.points.shouldBe(1)
      Cha2ds2VascRiskFactor.Age75OrOlder.points.shouldBe(2)
      Cha2ds2VascRiskFactor.PriorStrokeTiaThromboembolism.points.shouldBe(2)
      Cha2ds2VascRiskFactor.Age65To74.description.shouldBe("age 65 to 74 years")
    }

    "carry stable output values for risk bands and status" in {
      Cha2ds2VascRiskBand.Low.outputValue.shouldBe("low")
      Cha2ds2VascRiskBand.Intermediate.outputValue.shouldBe("intermediate")
      Cha2ds2VascRiskBand.High.outputValue.shouldBe("high")
      Cha2ds2VascStatus.InsufficientData.outputValue.shouldBe("insufficient_data")
    }

    "derive low risk for a score of 0 in a non-female patient" in {
      Cha2ds2VascRiskBand.fromTotal(0, SexCategory.NotFemale).shouldBe(Cha2ds2VascRiskBand.Low)
    }

    "derive low risk for a score of 1 in a female patient" in {
      Cha2ds2VascRiskBand.fromTotal(1, SexCategory.Female).shouldBe(Cha2ds2VascRiskBand.Low)
    }

    "derive intermediate risk for a score of 1 in a non-female patient" in {
      Cha2ds2VascRiskBand.fromTotal(1, SexCategory.NotFemale).shouldBe(Cha2ds2VascRiskBand.Intermediate)
    }

    "derive intermediate risk for a score of 2 in a female patient" in {
      Cha2ds2VascRiskBand.fromTotal(2, SexCategory.Female).shouldBe(Cha2ds2VascRiskBand.Intermediate)
    }

    "derive high risk for a score of 2 in a non-female patient" in {
      Cha2ds2VascRiskBand.fromTotal(2, SexCategory.NotFemale).shouldBe(Cha2ds2VascRiskBand.High)
    }

    "derive high risk for a score of 3 in a female patient" in {
      Cha2ds2VascRiskBand.fromTotal(3, SexCategory.Female).shouldBe(Cha2ds2VascRiskBand.High)
    }

    "reject negative totals when deriving a risk band" in {
      an [IllegalArgumentException] should be thrownBy {
        Cha2ds2VascRiskBand.fromTotal(-1, SexCategory.NotFemale)
      }
    }
    "derive age risk factors from age" in {
      Cha2ds2VascRiskFactor.fromAge(64).shouldBe(None)
      Cha2ds2VascRiskFactor.fromAge(65).shouldBe(Some(Cha2ds2VascRiskFactor.Age65To74))
      Cha2ds2VascRiskFactor.fromAge(74).shouldBe(Some(Cha2ds2VascRiskFactor.Age65To74))
      Cha2ds2VascRiskFactor.fromAge(75).shouldBe(Some(Cha2ds2VascRiskFactor.Age75OrOlder))
    }

    "ignore negative ages when deriving an age risk factor" in {
      Cha2ds2VascRiskFactor.fromAge(-1).shouldBe(None)
    }

    "derive the sex-category risk factor when applicable" in {
      SexCategory.Female.riskFactor.shouldBe(Some(Cha2ds2VascRiskFactor.SexCategoryFemale))
      SexCategory.NotFemale.riskFactor.shouldBe(None)
    }

    "parse sex category inputs" in {
      SexCategory.parse("female").shouldBe(Some(SexCategory.Female))
      SexCategory.parse("F").shouldBe(Some(SexCategory.Female))
      SexCategory.parse("male").shouldBe(Some(SexCategory.NotFemale))
      SexCategory.parse("M").shouldBe(Some(SexCategory.NotFemale))
      SexCategory.parse("not_female").shouldBe(Some(SexCategory.NotFemale))
      SexCategory.parse("unknown").shouldBe(None)
    }

  }
