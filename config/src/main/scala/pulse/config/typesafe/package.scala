package pulse.config

import com.typesafe.config.ConfigFactory
import fs2.Task

package object typesafe {
  import Task._

  implicit val immutableBuilder: ImmutableBuilder = new ImmutableBuilder {
    override def apply(source: Source): Task[Conf] = for {
      c <- delay {
        source match {
          case Source.Classpath     => ConfigFactory.defaultApplication()
          case Source.FileSource(f) => ConfigFactory.parseFile(f)
          case Source.Raw(config)   => ConfigFactory.parseString(config)
        }
      }
    } yield new TypesafeConf(c)
  }

}
