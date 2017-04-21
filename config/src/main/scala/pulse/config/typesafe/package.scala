package pulse.config

import java.io.File

import cats.implicits._
import com.typesafe.config.ConfigFactory
import fs2.Task
import fs2.util.Attempt
import pulse.common.exceptions.{ConfigException, NotSupportedException}

import scala.util.{Failure, Success, Try}

package object typesafe {

  import Task._

  val defaultConfigName = "application.conf"

  implicit val immutableBuilder: ImmutableBuilder = new ImmutableBuilder {
    def apply(source: Source): Task[Conf] = for {
      c <- delay {
        source match {
          case Source.Classpath => ConfigFactory.defaultApplication()
          case Source.FileSource(f) => ConfigFactory.parseFile(f)
          case Source.Raw(config) => ConfigFactory.parseString(config)
        }
      }
    } yield new TypesafeConf(c)
  }

  implicit val mutableBuilder: MutableBuilder = new MutableBuilder {
    def apply(source: Source): fs2.Stream[Task, Attempt[Conf]] = source match {
      case Source.FileSource(f) => Listeners.file(f.toPath).map {
        _ => Try(ConfigFactory.parseFile(f)) match {
          case Success(s) => Attempt(new TypesafeConf(s))
          case Failure(err) => Left(err)
        }
      }

      case Source.Classpath =>
        ClassLoader.getSystemClassLoader.getResource(defaultConfigName).some match {
          case Some(url) => apply(Source.FileSource(new File(url.getFile)))
          case None => fs2.Stream.eval(fail(ConfigException(s"Default config $defaultConfigName has not found on the classpath")))
        }

      case x => fs2.Stream.eval(fail(NotSupportedException(s"Source '$x' is not supported")))
    }
  }

}
