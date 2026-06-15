package org.aurora.sjsast


import org.scalatest._

import wordspec._
import matchers._
import scala.concurrent.Future
import org.aurora.utils.fileutils.createFileIfNotExists
import scala.concurrent.ExecutionContext
import scala.scalajs.js



class BaseAsyncTest extends wordspec.AsyncWordSpec with should.Matchers with TestFileUtils:
  export JoinMeet.given

  export scala.concurrent.Future
  export scala.scalajs.js.JSConverters._ 

  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  override implicit def executionContext: ExecutionContext = queue


  //info() wrapped in a future
  protected def finfo(output:String) =  Future(info(s"$output"))

  def ftestfilepath(index:Int) = Future(testfilepath(index))  
  def ftestfiletext(index:Int) = Future(testfiletext(index))  




  import typings.auroraLangium.distTypesSrcLanguageGeneratedAstMod as GenAstMod
  def ir(index:Int): Future[Option[PCM]] = parse(index).map{
    case None => fail("Parsing returned None")
    case Some(pcm) => Some(PCM(pcm))  
    }

  def parse(index:Int): Future[Option[GenAstMod.PCM]] = fileutils.parse(testfilepath(index)).toFuture.recover(
      {
        case _: js.JavaScriptException => 
          // Handle JavaScript parsing errors
          fail("Parse failed with JavaScript error")
          None
        case ex: Exception => 
          // Handle other exceptions
          fail(s"Parse failed: ${ex.getMessage}")
          None
      }
    ).map { 
      case None => None
      case pcm => Some(pcm.asInstanceOf[GenAstMod.PCM])
    }

