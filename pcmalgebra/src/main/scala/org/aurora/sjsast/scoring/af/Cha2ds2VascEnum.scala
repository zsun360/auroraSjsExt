package org.aurora.sjsast.scoring.af

private def normalizeCha2ds2VascParserInput(input: String): String =
  input
    .trim
    .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
    .toLowerCase
    .replaceAll("[^a-z0-9]+", "_")
    .stripPrefix("_")
    .stripSuffix("_")


enum SexCategory(val points: Int, val description: String):
  case Female extends SexCategory(1, "female sex category")
  case NotFemale extends SexCategory(0, "not female sex category")

  def riskFactor: Option[Cha2ds2VascRiskFactor] =
    this match
      case SexCategory.Female => Some(Cha2ds2VascRiskFactor.SexCategoryFemale)
      case SexCategory.NotFemale => None

object SexCategory:
  def parse(input: String): Option[SexCategory] =
    normalizeCha2ds2VascParserInput(input) match
      case "female" | "f" | "woman" => Some(SexCategory.Female)
      case "male" | "m" | "man" | "not_female" => Some(SexCategory.NotFemale)
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

  def totalPoints(riskFactors: Iterable[Cha2ds2VascRiskFactor]): Int =
    riskFactors.toSet.iterator.map(_.points).sum

  def parse(input: String): Option[Cha2ds2VascRiskFactor] =
    normalizeCha2ds2VascParserInput(input) match
      case "congestive_heart_failure" | "heart_failure" | "chf" | "hf" =>
        Some(Cha2ds2VascRiskFactor.CongestiveHeartFailure)
      case "hypertension" | "htn" =>
        Some(Cha2ds2VascRiskFactor.Hypertension)
      case "age_75_years_or_older" | "age_75_or_older" | "age_75" | "age75_or_older" | "age75" =>
        Some(Cha2ds2VascRiskFactor.Age75OrOlder)
      case "diabetes" | "diabetes_mellitus" | "dm" =>
        Some(Cha2ds2VascRiskFactor.DiabetesMellitus)
      case "prior_stroke_tia_te" | "prior_stroke_tia_thromboembolism" |
          "prior_stroke" | "prior_tia" | "stroke" | "tia" | "thromboembolism" | "te" =>
        Some(Cha2ds2VascRiskFactor.PriorStrokeTiaThromboembolism)
      case "vascular_disease" | "cad" | "coronary_artery_disease" |
          "mi" | "myocardial_infarction" | "angina" | "pci" | "cabg" |
          "peripheral_vascular_disease" | "pvd" =>
        Some(Cha2ds2VascRiskFactor.VascularDisease)
      case "age_65_to_74_years" | "age_65_to_74" | "age_65_74" | "age65_to_74" | "age65_74" =>
        Some(Cha2ds2VascRiskFactor.Age65To74)
      case "female_sex_category" | "sex_category_female" | "female_sex" =>
        Some(Cha2ds2VascRiskFactor.SexCategoryFemale)
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
    normalizeCha2ds2VascParserInput(input) match
      case "low" => Some(Cha2ds2VascRiskBand.Low)
      case "intermediate" => Some(Cha2ds2VascRiskBand.Intermediate)
      case "high" => Some(Cha2ds2VascRiskBand.High)
      case _ => None


enum Cha2ds2VascStatus(val outputValue: String):
  case InsufficientData extends Cha2ds2VascStatus("insufficient_data")

object Cha2ds2VascStatus:
  def fromOutputValue(input: String): Option[Cha2ds2VascStatus] =
    normalizeCha2ds2VascParserInput(input) match
      case "insufficient_data" => Some(Cha2ds2VascStatus.InsufficientData)
      case _ => None
