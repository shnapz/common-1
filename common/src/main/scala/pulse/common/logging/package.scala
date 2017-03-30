package pulse.common

import cats.Show
import fs2.Task
import org.log4s.Logger

package object logging extends DefaultDescriptors {

  /**
   * Logs the specified message with an [INFO] log-level inside a task-based computation
   */
  def info (message: String)(implicit L: Logger): Task[Unit] = Task.delay {
    L.info(message)
  }

  /**
   * Logs the specified message with a [WARN] log-level inside a task-based computation
   */
  def warn (message: String)(implicit L: Logger): Task[Unit] = Task.delay {
    L.warn(message)
  }

  /**
   * Logs the specified message with a [ERROR] log-level inside a task-based computation
   */
  def error(message: String)(implicit L: Logger): Task[Unit] = Task.delay {
    L.error(message)
  }

  /**
   * Logs the specified message with a [TRACE] log-level inside a task-based computation
   */
  def trace(message: String)(implicit L: Logger): Task[Unit] = Task.delay {
    L.trace(message)
  }

  /**
   * Logs the specified message with a [DEBUG] log-level inside a task-based computation
   */
  def debug(message: String)(implicit L: Logger): Task[Unit] = Task.delay {
    L.debug(message)
  }

  /**
   * Describes an instance of the specified type using the implicitly available instance of a Show for this type
   */
  def describe[A](instance: A)(implicit S: Show[A]): String = S.show(instance)
}
