package pulse
package config

import fs2.Task
import fs2.util.Attempt
import pulse.common._

object Conf {

  /**
   * Creates an immutable configuration from the specified source
   */
  def immutable (source: Source)(implicit from: ImmutableBuilder) = from(source)

  /**
   * Creates a mutable configuration from the specified source.
   *
   * Mutable configuration is able to subscribe to updates of the source and notify subscribers each time the source is updated
   */
  def mutable (source: Source)(implicit from: MutableBuilder) = from(source)

}

abstract class Conf {

  def get[T](path: NeString)(implicit D: TypeDescriptor[T]): Task[T]

  def lookup[T](path: NeString)(implicit D: TypeDescriptor[T]): Task[Option[T]]

  def subconfig(path: NeString): Task[Conf]

}

trait ImmutableBuilder {
  def apply(source: Source): Task[Conf]
}

trait MutableBuilder {
  def apply(source: Source): fs2.Stream[Task, Attempt[Conf]]
}
