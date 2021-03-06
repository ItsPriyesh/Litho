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

package me.priyesh.litho
package core

import java.io.File
import java.nio.file._

import me.priyesh.litho.Strings._
import me.priyesh.litho.core.FontStyle._

object Packager {

  def buildPackage(folderName: String): CanFail = {
    if (!FontLoader.folderExists(folderName)) {
      println(ErrorCantFindFolder)
      failed
    } else {
      val filesAndStyles = FontLoader.filesAndStylesFromFolder(folderName)
      if (filesAndStyles.exists(f => !Verifier.fontIsValid(f._1, f._2))) {
        println(WarningInvalidMacStyles)
        failed
      } else {
        packageFonts(folderName, filesAndStyles.map(_.swap).toMap)
        println(PackageWasCreated)
        succeeded
      }
    }
  }

  private def enoughStylesProvided(styles: Set[FontStyle]): Boolean = BasicStyles subsetOf styles

  private def packageFonts(folderName: String, stylesToFiles: Map[FontStyle, File]): CanFail = {
    val packagedFolder = new File(s"./${folderName}FontPack/")
    packagedFolder.mkdirs()

    val providedStyles = stylesToFiles.keySet

    def createDestinationFile(name: String): File = new File(s"${packagedFolder.getPath}/$name")

    if (enoughStylesProvided(providedStyles)) {
      // Copy all the styles that have already been provided
      stylesToFiles.foreach(styleToFile => copyFile(styleToFile._2, createDestinationFile(styleToFile._1.name)))

      // Aggregate the styles that haven't been provided
      val stylesToGenerate = AllStyles diff providedStyles
      stylesToGenerate.foreach(style => {
        copyFile(stylesToFiles.get(getFallbackStyle(style, providedStyles)).get, createDestinationFile(style.name))
      })
      succeeded
    } else {
      println(ErrorNotEnoughStylesProvided)
      failed
    }
  }

  private def getFallbackStyle(style: FontStyle, providedStyles: Set[FontStyle]): FontStyle =
    StyleFallbackMap.get(style).get.find(providedStyles.contains).get

  private def copyFile(source: File, dest: File): CanFail = {
    def fileToPath(file: File): Path = Paths.get(file.toURI)
    CanFail(Files.copy(fileToPath(source), fileToPath(dest), StandardCopyOption.REPLACE_EXISTING))
  }

}
