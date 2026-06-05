package org.aurora.sjsast.scoring.af

enum SexCategory(val points: Int, val description: String):
  case Female extends SexCategory(1, "female sex category")
  case NotFemale extends SexCategory(0, "not female sex category")

  def riskFactor: Option[Cha2ds2VascRiskFactor] =
    this match
      case SexCategory.Female => Some(Cha2ds2VascRiskFactor.SexCategoryFemale)
      case SexCategory.NotFemale => None

object SexCategory:
  def parse(input: String): Option[SexCategory] =
    input.trim.toLowerCase match
      case "female" | "f" => Some(SexCategory.Female)
      case "male" | "m" | "not female" | "not_female" => Some(SexCategory.NotFemale)
      case _ => None


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

object Cha2ds2VascRiskFactor:
  def fromAge(age: Int): Option[Cha2ds2VascRiskFactor] =
    if age < 0 then None
    else if age >= 75 then Some(Cha2ds2VascRiskFactor.Age75OrOlder)
    else if age >= 65 then Some(Cha2ds2VascRiskFactor.Age65To74)
    else None

  def parse(input: String): Option[Cha2ds2VascRiskFactor] =
    input.trim.toLowerCase.replace(" ", "_").replace("-", "_") match
      case "congestive_heart_failure" | "heart_failure" | "chf" =>
        Some(Cha2ds2VascRiskFactor.CongestiveHeartFailure)
      case "hypertension" | "htn" =>
        Some(Cha2ds2VascRiskFactor.Hypertension)
      case "diabetes" | "diabetes_mellitus" | "dm" =>
        Some(Cha2ds2VascRiskFactor.DiabetesMellitus)
      case "prior_stroke" | "prior_tia" | "stroke" | "tia" | "thromboembolism" =>
        Some(Cha2ds2VascRiskFactor.PriorStrokeTiaThromboembolism)
      case "vascular_disease" | "cad" | "coronary_artery_disease" | "mi" | "pvd" =>
        Some(Cha2ds2VascRiskFactor.VascularDisease)
      case _ =>
        None


enum Cha2ds2VascRiskBand(val outputValue: String):
  case Low extends Cha2ds2VascRiskBand("low")
  case Intermediate extends Cha2ds2VascRiskBand("intermediate")
  case High extends Cha2ds2VascRiskBand("high")

object Cha2ds2VascRiskBand:
  def fromTotal(total: Int, sexCategory: SexCategory): Cha2ds2VascRiskBand =
    require(total >= 0, "CHA2DS2-VASc total cannot be negative")

    sexCategory match
      case SexCategory.NotFemale =>
        total match
          case 0 => Cha2ds2VascRiskBand.Low
          case 1 => Cha2ds2VascRiskBand.Intermediate
          case _ => Cha2ds2VascRiskBand.High
      case SexCategory.Female =>
        total match
          case 0 | 1 => Cha2ds2VascRiskBand.Low
          case 2 => Cha2ds2VascRiskBand.Intermediate
          case _ => Cha2ds2VascRiskBand.High


  def fromOutputValue(input: String): Option[Cha2ds2VascRiskBand] =
    input.trim.toLowerCase match
      case "low" => Some(Cha2ds2VascRiskBand.Low)
      case "intermediate" => Some(Cha2ds2VascRiskBand.Intermediate)
      case "high" => Some(Cha2ds2VascRiskBand.High)
      case _ => None


enum Cha2ds2VascStatus(val outputValue: String):
  case InsufficientData extends Cha2ds2VascStatus("insufficient_data")

object Cha2ds2VascStatus:
  def fromOutputValue(input: String): Option[Cha2ds2VascStatus] =
    input.trim.toLowerCase match
      case "insufficient_data" | "insufficient data" => Some(Cha2ds2VascStatus.InsufficientData)
      case _ => None
