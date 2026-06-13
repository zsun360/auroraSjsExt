package org.aurora.sjsast.arnold

import org.scalatest._
import wordspec._
import matchers._
import org.aurora.sjsast._


class ArnoldParametricModelingTest extends BaseAsyncTest:
  "This" should {
    "work" in {

      import org.aurora.sjsast.Show.*
      for {
        _     <- finfo(s"Running test: testfilename:")
        _     <- finfo(s"${testfilepath(0)}")
        pcm       <- ir(0)
        _      <- finfo(s"Parse Result:")
        _       <- finfo(s"${pcm}")
        _    <- finfo(s"Parse Result ${Show.show(pcm)}:")
      }  yield(true should be(true))

     
    }
  }
  