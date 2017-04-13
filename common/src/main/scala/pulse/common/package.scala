package pulse

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

package object common {

  type NeString = String Refined NonEmpty

  implicit def neStringToString(input: NeString): String = input.value
}
