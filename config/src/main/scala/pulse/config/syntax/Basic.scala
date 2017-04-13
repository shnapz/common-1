package pulse.config
package syntax

import cats.data.Kleisli
import fs2.Task

trait Basic {

  implicit def _extendR[U](reader: ReaderConf[U]): ReaderExtensions[U] = new ReaderExtensions[U](reader)

}

final class ReaderExtensions[U](val self: ReaderConf[U]) extends AnyVal {

  def =<< (input: Task[Conf]) = input flatMap self.run

}
