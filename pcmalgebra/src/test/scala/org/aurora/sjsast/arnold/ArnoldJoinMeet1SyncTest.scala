package org.aurora.sjsast.arnold

import org.scalatest._
import wordspec._
import matchers._
import org.aurora.sjsast._

class ArnoldJoinMeet1SyncTest extends BaseSyncTest :
  "type class derivation for Int and String" should {
    "work" in {
      import Join1.{given,*}
      import Show.{given,*}


      case class B(s:String, s1:String)
      val b1 = B("a", "x")
      val b2 = B("b", "y")
      val b3 = B("b", "z")


      info(s"${b1 |+| b2 |+| b3} ")
   }
  }

  "typeclass derivation for LHSet" should {
    "work like this" in {
      //TODO FINISH THIS
      // 1. Define the type alias
      import scala.collection.mutable.{LinkedHashSet, LinkedHashMap}

      trait NamedItem:
        val name: String
      type LHSet1[T <: NamedItem] = LinkedHashSet[T]

      case class NamedString(name: String, value: String) extends NamedItem
      case class NamedInt(name: String, value: Int) extends NamedItem

    





    }
  }  