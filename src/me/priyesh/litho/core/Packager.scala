/*
 * Copyright 2015 Priyesh Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.priyesh.litho.core

import java.io.File
import java.nio.file._

import me.priyesh.litho.Strings

object Packager {

  private def containsInvalidFiles(files: List[File]): Boolean = files.exists(!_.getName.toUpperCase.endsWith(".TTF"))

  private def invalidFileCount(files: List[File]): Boolean = files.size != FontStyle.BasicStyles.size

  private def folderExists(name: String): Boolean = new File(s"./$name").exists()

 /* private def findFile(files: List[File], search: String, exclude: Option[String] = None): Option[File] = {
    files.find(file => {
      val name = file.getName.toUpperCase
      if (exclude.isEmpty) name.contains(search.toUpperCase)
      else name.contains(search.toUpperCase) && !name.contains(exclude.get.toUpperCase)
    })
  }
*/
  def buildPackageFromBasics(folderName: String): Unit = {
    import Strings._
    import Verifier._

    if (!folderExists(folderName)) {
      println(ErrorCantFindFolder)
    } else {
      val files = FontLoader.filesFromFolder(folderName)
      if (containsInvalidFiles(files)) {
        println(ErrorInvalidFiles)
      } else if (invalidFileCount(files)) {
        println(s"$ErrorInvalidFileCount\n$ErrorEnsureBasicsExist")
      } else {
        val basicFiles = findBasicFiles(files)
        if (basicFiles.nonEmpty) {
          val filesAndStyles = basicFiles zip FontStyle.BasicStyles
          if (filesAndStyles.forall(fontIsValid _ tupled)) {
            generateDerivatives(folderName, filesAndStyles)
            println(PackageWasCreated)
          } else {
            println(ErrorInvalidMacStyles)
          }
        } else {
          println(s"$ErrorBasicsMissing\n$ErrorEnsureBasicsExist")
        }
      }
    }
  }

  private def generateDerivatives(folderName: String, basics: List[(File, FontStyle)]): Unit = {
    import FontStyle._

    new File(s"./${folderName}Generated/").mkdirs()

    def buildDestFile(style: FontStyle): File = new File(s"./${folderName}Generated/${style.name}")

    basics.foreach(styleAndFile => {
      val basicFile = styleAndFile._1
      val basicStyle = styleAndFile._2
      val derivatives = DerivativeMap.getOrElse(basicStyle, List())
      derivatives.foreach(style => copyFile(basicFile, buildDestFile(style)))
    })
  }

  private def copyFile(source: File, dest: File): Unit = {
    def fileToPath(file: File): Path = Paths.get(file.toURI)
    Files.copy(fileToPath(source), fileToPath(dest), StandardCopyOption.REPLACE_EXISTING)
  }

  private def findBasicFiles(files: List[File]): List[File] = {
    val regular = files.find(_.getName equalsIgnoreCase "Roboto-Regular.ttf")
    val italic = files.find(_.getName equalsIgnoreCase "Roboto-Italic.ttf")
    val bold = files.find(_.getName equalsIgnoreCase "Roboto-Bold.ttf")
    val boldItalic = files.find(_.getName equalsIgnoreCase "Roboto-BoldItalic.ttf")

    val basics = List(regular, italic, bold, boldItalic)
    if (basics.exists(_.isEmpty)) List()
    else basics.flatten
  }

}
