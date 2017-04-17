package pulse
package config

import cats.data.Kleisli
import fs2.Task
import fs2.interop.cats._

import common._

object readers {

  val conf: ReaderConf[Conf] = Kleisli.ask[Task, Conf]

  def get[T](path: NeString)(implicit D: TypeDescriptor[T]): ReaderConf[T] = Kleisli {
    c => c.get[T](path)
  }

  def lookup[T](path: NeString)(implicit D: TypeDescriptor[T]): ReaderConf[Option[T]] = Kleisli {
    c => c.lookup[T](path)
  }

  def subconfig(path: NeString): ReaderConf[Conf] = Kleisli {
    c => c.subconfig(path)
  }

  implicit def _wrapL[U](expr: Task[U]): ReaderConf[U] = Kleisli.lift(expr)

}
