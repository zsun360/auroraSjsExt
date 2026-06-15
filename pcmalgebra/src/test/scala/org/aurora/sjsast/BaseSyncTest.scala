package org.aurora.sjsast


import org.scalatest._

import wordspec._
import matchers._
import scala.concurrent.Future
import org.aurora.utils.fileutils.createFileIfNotExists
import scala.scalajs.js
export org.aurora.utils.{fileutils,fs}


//for testing that does not involve parsing which is async (via js.Promise)
//note that not test can involve parsing because that is async
//technically doesnot need TestFileUtils because there is no parsing
class BaseSyncTest extends AnyWordSpec with should.Matchers // with TestFileUtils:




