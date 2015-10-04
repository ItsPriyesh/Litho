package me.priyesh

import com.google.typography.font.sfntly.table.core.FontHeaderTable
import com.google.typography.font.sfntly.{Font, Tag}

object Verifier {

  import FontStyle._

  def fontIsValid(font: Font, style: FontStyle): Boolean = {
    val macStyle = font.getTable[FontHeaderTable](Tag.head).macStyleAsInt()
    val requiredFlags = getRequiredFlags(style)
    val allNeededFlags = requiredFlags.forall(flag => (macStyle & (1 << flag)) != 0)
    val noExtraFlags = Set(0, 1).diff(requiredFlags).forall(flag => (macStyle & (1 << flag)) == 0)
    allNeededFlags && noExtraFlags
  }

  private def getRequiredFlags(style: FontStyle): Set[Int] = style match {
    case Black | Bold | CondensedBold => Set(0)
    case Italic | CondensedItalic | CondensedLightItalic | LightItalic | MediumItalic | ThinItalic => Set(1)
    case CondensedBoldItalic | BoldItalic | BlackItalic => Set(0, 1)
    case CondensedRegular | Regular | Thin | Medium | Light | CondensedLight => Set()
  }

  private def getCorrectMacStyle(style: FontStyle): Int =
    getRequiredFlags(style).foldLeft(0) { (macStyleSoFar, newFlag) => macStyleSoFar | 1 << newFlag }
}