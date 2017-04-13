package pulse
package config

import fs2.Task

import common._

object Conf {

  def immutable (source: Source)(implicit from: ImmutableBuilder) = from(source)

}

abstract class Conf {

  def get[T](path: NeString)(implicit D: TypeDescriptor[T]): Task[T]

  def lookup[T](path: NeString)(implicit D: TypeDescriptor[T]): Task[Option[T]]

  def subconfig(path: NeString): Task[Conf]

}

trait ImmutableBuilder {
  def apply(source: Source): Task[Conf]
}
