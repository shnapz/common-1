package pulse.examples.configs.mutable

import java.io.File
import org.log4s

import fs2.{Stream, Task}
import fs2.interop.cats._

import eu.timepit.refined.auto._

import pulse.common.exceptions._
import pulse.common.logging
import pulse.common._

import pulse.config.{Conf, Source}
import pulse.config.typesafe._
import pulse.config.syntax._
import pulse.config.readers._

object Main extends Runner {

  implicit val L = log4s.getLogger

  val reader = for {
    _ <- conf
    a <- get[String]("config.testA")
    b <- get[String]("config.testB")
    s <- subconfig("config.subconfig")
    c <- s.get[String]("testC")
  } yield (a,b,c)

  def program(from: File) = for {
    c <- Conf.mutable(Source.FileSource(from))
    v <- Stream.eval(reader(c))
    _ <- Stream.eval(logging.info(s"$v"))
  } yield ()

  protected def run(args: List[String]): Task[Unit] = args match {
    case h :: _ => program(new File(h)).run
    case Nil    => Task.fail(NotSupportedException("filename is required"))
  }
}
