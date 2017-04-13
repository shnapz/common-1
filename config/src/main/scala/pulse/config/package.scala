package pulse

import cats.data.ReaderT
import fs2.Task

package object config {

  type ReaderConf[U] = ReaderT[Task, Conf, U]
}
