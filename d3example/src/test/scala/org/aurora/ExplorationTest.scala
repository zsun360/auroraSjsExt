package org.aurora


class ProjectEulerTest extends BasicTest {

  "project euler #1 " should {
    "3 or 5" in {
      (1 to 999).filter{n => n % 3 == 0 || n % 5 == 0}.sum should be (233168)     


      (3 to 999 by 3).sum + (5 to 999 by 5).sum - (15 to 999 by 15).sum should be (233168)

      val size = (3 to 999).count{n => n % 3 == 0 || n % 5 == 0}
      info (s"size: $size")

    }
  }
}