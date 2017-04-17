package pulse.common

import org.log4s
import fs2.Task

import logging._
import syntax._

abstract class Runner {

  private implicit val L = log4s.getLogger

  final def main(args: Array[String]): Unit = run(args.toList).unsafeAttemptRun() match {
    case Left (f) => L.error(s"Unexpected exception during application run. ${logging.describe(f)}"); System.exit(1)
    case Right(_) => L.info(s"System has been shutdown successfully")
  }

  protected def run (args: List[String]): Task[Unit]

}

abstract class ContextRunner[A] extends Runner {
  import fs2.interop.cats._

  private implicit val L = log4s.getLogger

  protected implicit def managedContext: Managed[A]

  protected def createContext(args: List[String]): Task[A]

  protected def execute(context: A): Task[Unit]

  override protected def run (args: List[String]) = bracket(createContext(args))(execute)

}
