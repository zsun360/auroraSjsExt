package org.aurora.sjsast

// Defining singleton for the state of incompleteness
sealed trait Incomplete
case object Incomplete extends Incomplete {
  override def toString: String = "???"
}

sealed trait Value {
  def asDouble: Option[Double] = this match {
    case IntValue(v)    => Some(v.toDouble)
    case DoubleValue(v) => Some(v)
    case BoolValue(v)   => Some(if (v) 1.0 else 0.0)
    case IncompleteValue => None
    case StringValue(v) => v.toDoubleOption
  }
}

case class IntValue(value: Int) extends Value
case class BoolValue(value: Boolean) extends Value
case class DoubleValue(value: Double) extends Value
case class StringValue(value: String) extends Value
case object IncompleteValue extends Value

object Value:
  def apply(v: Any): Value = v match
    case b: Boolean => BoolValue(b)
    case n: Double if n % 1 == 0 => IntValue(n.toInt)
    case n: Double => DoubleValue(n)
    case s: String if s == "???" => IncompleteValue
    case s: String => StringValue(s)
    case _: Incomplete => IncompleteValue
    case _ => StringValue(v.toString)