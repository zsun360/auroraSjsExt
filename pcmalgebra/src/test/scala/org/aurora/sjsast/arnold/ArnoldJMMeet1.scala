package org.aurora.sjsast.arnold
import magnolia1._

trait Meet1[T]:
  def meet(a: T, b: T): T


object Meet1 extends AutoDerivation[Meet1]:
  val hey = 0;
  extension [T](a: T)(using jm: Meet1[T])
    def |&| (b: T): T = jm.meet(a, b)

  override def join[T](ctx: CaseClass[Meet1, T]): Meet1[T] = (a, b) =>
    ctx.construct { param =>
      param.typeclass.meet(param.deref(a), param.deref(b))
    }

  override def split[T](ctx: SealedTrait[Meet1, T]): Meet1[T] = (a, b) =>
    ctx.choose(a) { sub =>
      if (sub.cast.isDefinedAt(b)) then
        sub.typeclass.meet(sub.value, sub.cast(b))
      else
        a 
    }  
  



