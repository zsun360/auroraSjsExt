package org.aurora.sjsast.arnold
import magnolia1._
import org.aurora.sjsast.LHSet

trait Join1[T]:
  def join(a: T, b: T): T

object Join1 extends AutoDerivation[Join1]:
  val hey = 0;
  extension [T](a: T)(using jm: Join1[T])
    def |+|(b: T): T = jm.join(a, b)


  override def join[T](ctx: CaseClass[Join1, T]): Join1[T] = (a, b) =>
    ctx.construct { param =>
      param.typeclass.join(param.deref(a), param.deref(b))
    }

  override def split[T](ctx: SealedTrait[Join1, T]): Join1[T] = (a, b) =>
    ctx.choose(a) { sub =>
      if (sub.cast.isDefinedAt(b)) then
        sub.typeclass.join(sub.value, sub.cast(b))
      else
        a 
    }  
  

trait NamedTrait:
  val name: String
  
given Join1[String] = (a, b) => 
  if (a == b) then a 
  else if (a.isEmpty) then b 
  else if (b.isEmpty) then a 
  else s"$a$b"

given [T <: NamedTrait]:Join1[LHSet[T]]  = ???  //TODO FINISH THIS
