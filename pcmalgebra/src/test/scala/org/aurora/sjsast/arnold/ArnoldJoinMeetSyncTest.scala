package org.aurora.sjsast.arnold

import org.scalatest._
import wordspec._
import matchers._
import org.aurora.sjsast._

class ArnoldJoinMeetSyncTest extends BaseSyncTest :
  "this" should {
    "join" in {
      import JoinMeet.{given,*}
      import Show.{given,*}

      case class A(x:Int)
      val a1 = A(1)
      val a2 = A(2)

      case class B(x:Int,s:String)
      val b1 = B(1,"a")
      val b2 = B(2,"b")
      val b3 = B(2,"b")



      a1 |+| a2 should be (A(3))

      info(s"${b1 |+| b2 |+| b3} ")



   }
  }