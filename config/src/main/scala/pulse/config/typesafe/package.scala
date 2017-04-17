package pulse.config

import com.typesafe.config.ConfigFactory
import fs2.Task
import fs2.util.Attempt
import pulse.common.exceptions.NotSupportedException

import scala.util.{Failure, Success, Try}

package object typesafe {
  import Task._

  implicit val immutableBuilder: ImmutableBuilder = new ImmutableBuilder {
    def apply(source: Source): Task[Conf] = for {
      c <- delay {
        source match {
          case Source.Classpath     => ConfigFactory.defaultApplication()
          case Source.FileSource(f) => ConfigFactory.parseFile(f)
          case Source.Raw(config)   => ConfigFactory.parseString(config)
        }
      }
    } yield new TypesafeConf(c)
  }


  implicit val mutableBuilder: MutableBuilder = new MutableBuilder {
    def apply(source: Source): fs2.Stream[Task, Task[Attempt[Conf]]] = source match {
      case Source.FileSource(f) => Listeners.file(f.toPath).map {
        _ => Try(ConfigFactory.parseFile(f)) match {
          case Success(s) => now(Attempt(new TypesafeConf(s)))
          case Failure(err) => fail(err)
        }
      }
      case x => fs2.Stream.eval(fail(NotSupportedException(s"Source '$x' is not supported")))
    }
  }

}
