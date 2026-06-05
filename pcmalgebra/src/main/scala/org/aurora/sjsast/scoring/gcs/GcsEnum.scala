package org.aurora.sjsast.scoring.gcs

trait GcsComponent:
  def score: Int
  def description: String


private def findGcsComponentByScore[A <: GcsComponent](values: Array[A], inputScore: Int): Option[A] =
  var result: Option[A] = Option.empty

  for component <- values
    if component.score == inputScore
    do result = Some(component)
  result


enum ComponentResolution[+T]:
  case Missing
  case NotTestable
  case Resolved(value: T)


enum Eye(val score: Int, val description: String) extends GcsComponent:
  case Spontaneous extends Eye(4, "eyes open spontaneously")
  case ToVoice extends Eye(3, "eyes open to voice or verbal command")
  case ToPain extends Eye(2, "eyes open to pain or pressure")
  case None extends Eye(1, "no eye opening")

object Eye:
  def findByScore(inputScore: Int): Option[Eye] =
    findGcsComponentByScore(Eye.values, inputScore)

enum Verbal(val score: Int, val description: String) extends GcsComponent:
  case Oriented extends Verbal(5, "oriented verbal response")
  case Confused extends Verbal(4, "confused verbal response")
  case Words extends Verbal(3, "inappropriate words")
  case Sounds extends Verbal(2, "incomprehensible sounds")
  case None extends Verbal(1, "no verbal response")

object Verbal:
  def findByScore(inputScore: Int): Option[Verbal] =
    findGcsComponentByScore(Verbal.values, inputScore)


enum Motor(val score: Int, val description: String) extends GcsComponent:
  case ObeysCommands extends Motor(6, "obeys commands")
  case LocalizesPain extends Motor(5, "localizes pain")
  case WithdrawsFromPain extends Motor(4, "withdraws from pain")
  case AbnormalFlexion extends Motor(3, "abnormal flexion")
  case Extension extends Motor(2, "extension response")
  case None extends Motor(1, "no motor response")

object Motor:
  def findByScore(inputScore: Int): Option[Motor] =
    findGcsComponentByScore(Motor.values, inputScore)


enum GcsSeverity(val outputValue: String):
  case Severe extends GcsSeverity("severe")
  case Moderate extends GcsSeverity("moderate")
  case Mild extends GcsSeverity("mild")

object GcsSeverity:
  def fromTotal(total: Int): Option[GcsSeverity] =
    if total < 3 || total > 15 then None
    else if total <= 8 then Some(GcsSeverity.Severe)
    else if total <= 12 then Some(GcsSeverity.Moderate)
    else Some(GcsSeverity.Mild)

  def fromOutputValue(input: String): Option[GcsSeverity] =
    input.trim.toLowerCase match
      case "severe" => Some(GcsSeverity.Severe)
      case "moderate" => Some(GcsSeverity.Moderate)
      case "mild" => Some(GcsSeverity.Mild)
      case _ => None


enum GcsStatus(val outputValue: String):
  case NotTestable extends GcsStatus("not_testable")
  case Incomplete extends GcsStatus("incomplete")


enum GcsTotalSource(val outputValue: String):
  case Derived extends GcsTotalSource("derived")
  case Manual extends GcsTotalSource("manual")
