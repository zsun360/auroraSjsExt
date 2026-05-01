package org.aurora.sjsast.scoring.af

enum SexCategory(val points: Int, val description: String):
  case Female extends SexCategory(1, "female sex category")
  case NotFemale extends SexCategory(0, "not female sex category")


enum Cha2ds2VascRiskFactor(val points: Int, val description: String):
  case CongestiveHeartFailure extends Cha2ds2VascRiskFactor(1, "congestive heart failure")
  case Hypertension extends Cha2ds2VascRiskFactor(1, "hypertension")
  case Age75OrOlder extends Cha2ds2VascRiskFactor(2, "age 75 years or older")
  case DiabetesMellitus extends Cha2ds2VascRiskFactor(1, "diabetes mellitus")
  case PriorStrokeTiaThromboembolism
      extends Cha2ds2VascRiskFactor(2, "prior stroke, TIA, or thromboembolism")
  case VascularDisease extends Cha2ds2VascRiskFactor(1, "vascular disease")
  case Age65To74 extends Cha2ds2VascRiskFactor(1, "age 65 to 74 years")
  case SexCategoryFemale extends Cha2ds2VascRiskFactor(1, "female sex category")


enum Cha2ds2VascRiskBand(val outputValue: String):
  case Low extends Cha2ds2VascRiskBand("low")
  case Intermediate extends Cha2ds2VascRiskBand("intermediate")
  case High extends Cha2ds2VascRiskBand("high")


enum Cha2ds2VascStatus(val outputValue: String):
  case InsufficientData extends Cha2ds2VascStatus("insufficient_data")

