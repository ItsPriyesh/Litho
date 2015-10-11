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

object Strings {
  val ErrorInvalidFiles = "Folder contains invalid fonts. Only .ttf files are allowed."
  val ErrorInvalidFileCount = "Folder contains invalid number of fonts."
  val ErrorBasicsMissing = "Litho was unable to locate all basic files."
  val ErrorEnsureBasicsExist = "Ensure that you include the following styles: Regular, Italic, Bold, BoldItalic."
  val ErrorInvalidMacStyles = "Some fonts were found to have incorrect macstyles assigned."
  val ErrorFolderUnspecified = "A folder must be specified"
  val ErrorTooManyArgs = "Too many arguments"
  val ErrorCantFindFolder = "Litho could not find the specified folder."
  val ErrorUnrecognizedStyle = "An unrecognized style was found.\nEnsure that fonts in the specified folder are named correctly."
  val ErrorNotEnoughStylesProvided = "Not enough styles were provided.\n" + ErrorEnsureBasicsExist

  val Verifying = "Verifying..."
  val Packaging = "Packaging..."
  val Fixing = "Fixing..."

  val PackageWasCreated = "Package was created"
  val FontFixingComplete = "All fonts with invalid macStyles have been fixed"
  val AllFontsValid = "All fonts were valid"
}
