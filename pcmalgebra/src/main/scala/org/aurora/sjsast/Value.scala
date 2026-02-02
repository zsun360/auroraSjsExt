package org.aurora.sjsast

sealed trait Value {
  def asDouble: Option[Double] = this match {
    case IntValue(v)    => Some(v.toDouble)
    case DoubleValue(v) => Some(v)
    case BoolValue(v)   => Some(if (v) 1.0 else 0.0)
    case StringValue(v) => scala.util.Try(v.toDouble).toOption
  }
}

case class IntValue(value: Int) extends Value
case class BoolValue(value: Boolean) extends Value
case class DoubleValue(value: Double) extends Value
case class StringValue(value: String) extends Value

object Value:
  def apply(v: Any): Value = v match
    case b: Boolean => BoolValue(b)
    case n: Double if n % 1 == 0 => IntValue(n.toInt)
    case n: Double => DoubleValue(n)
    case s: String => StringValue(s)
    case _ => StringValue(v.toString)