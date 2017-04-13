package pulse.common

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty

object NonEmptyString {
  def apply(input: String Refined NonEmpty) = new NonEmptyString(input)
}

final class NonEmptyString private (private val content: String Refined NonEmpty) {

  def value = content.value

  override def hashCode(): Int = content.hashCode

  override def equals(obj: scala.Any): Boolean = content.equals(obj)

  override def toString: String = content.value

}
