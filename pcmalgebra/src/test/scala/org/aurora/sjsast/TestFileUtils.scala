package org.aurora.sjsast
import org.aurora.utils.{fileutils,fs}
import fileutils.createFileIfNotExists

trait TestFileUtils:
  protected lazy val testResourcesPath = fileutils.testResourcesPath
  protected lazy val basefilename = this.getClass.getSimpleName.replace("Test","")
  protected lazy val fullyQualifiedName = this.getClass.getName.replace("Test","").replace(".",fileutils.separator)
  protected lazy val testPath = s"$testResourcesPath${fileutils.separator}$fullyQualifiedName"
    def testfilepath(index:Int) = 
    val path =  s"$testResourcesPath${fileutils.separator}$fullyQualifiedName-$index.aurora"
    createFileIfNotExists(path)
    path


  def testfiletext(index:Int) = 
    val path = testfilepath(index)
    createFileIfNotExists(path)
    fileutils.readFileSync(path)


