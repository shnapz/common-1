package pulse.common.syntax

import fs2.Strategy

trait Strategies {
  implicit val strategy = Strategy.fromFixedDaemonPool(Runtime.getRuntime.availableProcessors)
}
