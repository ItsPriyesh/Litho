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

package me.priyesh

import java.io.File

object Packager {

  private def containsInvalidFiles(files: List[File]): Boolean = files.exists(!_.getName.toUpperCase.endsWith(".TTF"))

  private def invalidFileCount(files: List[File]): Boolean = files.size != FontStyle.BasicStyles.size

  private def findFile(files: List[File], search: String, exclude: Option[String] = None): Option[File] = {
    files.find(file => {
      val name = file.getName.toUpperCase
      if (exclude.isEmpty) name.contains(search)
      else name.contains(search) && !name.contains(exclude)
    })
  }

  def buildPackageFromBasics(files: List[File]): Unit = {
    import ErrorStrings._

    if (containsInvalidFiles(files)) println(InvalidFiles)
    else if (invalidFileCount(files)) println(s"$InvalidFileCount\n$EnsureBasicsExist")
    else {
      val basicFiles = findBasicFiles(files)
      if (basicFiles.isEmpty) {
        println(s"$BasicsMissing\n$EnsureBasicsExist")
      } else {

      }
    }
  }

  def findBasicFiles(files: List[File]): List[File] = {
    val regular = findFile(files, "regular")
    val italic = findFile(files, "italic", exclude = Some("bold"))
    val bold = findFile(files, "bold", exclude = Some("italic"))
    val boldItalic = findFile(files, "bolditalic")

    val basics = List(regular, italic, bold, boldItalic)
    if (basics.exists(_.isEmpty)) List()
    else basics.flatten
  }

}