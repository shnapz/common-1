package pulse.config
package typesafe

import scala.collection.JavaConverters._

import com.typesafe.config.Config
import fs2.Task

import pulse.common._
import pulse.common.exceptions._

final class TypesafeConf(self: Config) extends Conf {
  import Task._

  def get[T](path: NeString)(implicit D: TypeDescriptor[T]) = self.hasPath(path) -> D.value match {
    case (exists, _) if !exists => fail(ConfigException(s"Unable to locate path '$path' in the configuration"))
    case (_, Values.String)     => delay { self.getString(path).asInstanceOf[T] }
    case (_, Values.Int)        => delay { self.getInt(path).asInstanceOf[T] }
    case (_, Values.Double)     => delay { self.getDouble(path).asInstanceOf[T] }
    case (_, Values.Long)       => delay { self.getLong(path).asInstanceOf[T] }
    case (_, Values.Boolean)    => delay { self.getBoolean(path).asInstanceOf[T] }
    case (_, Values.AnyRef)     => delay { self.getAnyRef(path).asInstanceOf[T] }
    case (_, Values.StringList)     => delay { self.getStringList(path).asScala.asInstanceOf[T] }
    case (_, Values.IntList)        => delay { self.getIntList(path).asScala.asInstanceOf[T] }
    case (_, Values.DoubleList)     => delay { self.getDoubleList(path).asScala.asInstanceOf[T] }
    case (_, Values.LongList)       => delay { self.getLongList(path).asScala.asInstanceOf[T] }
    case (_, Values.BooleanList)    => delay { self.getBooleanList(path).asScala.asInstanceOf[T] }
    case (_, Values.AnyRefList)     => delay { self.getAnyRefList(path).asScala.asInstanceOf[T] }
    case (_, Values.Unknown)    => fail(ConfigException(s"Unable to recognize value for extraction"))
  }

  def lookup[T](path: NeString)(implicit D: TypeDescriptor[T]) = self.hasPathOrNull(path) -> D.value match {
    case (exists, _) if !exists => now(None)
    case (_, Values.String)     => delay { Option(self.getString(path)).map(_.asInstanceOf[T]) }
    case (_, Values.Int)        => delay { Option(self.getInt(path)).map(_.asInstanceOf[T]) }
    case (_, Values.Double)     => delay { Option(self.getDouble(path)).map(_.asInstanceOf[T]) }
    case (_, Values.Long)       => delay { Option(self.getLong(path)).map(_.asInstanceOf[T]) }
    case (_, Values.Boolean)    => delay { Option(self.getBoolean(path)).map(_.asInstanceOf[T]) }
    case (_, Values.AnyRef)     => delay { Option(self.getAnyRef(path)).map(_.asInstanceOf[T]) }
    case (_, Values.StringList)     => delay { Option(self.getStringList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.IntList)        => delay { Option(self.getIntList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.DoubleList)     => delay { Option(self.getDoubleList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.LongList)       => delay { Option(self.getLongList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.BooleanList)    => delay { Option(self.getBooleanList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.AnyRefList)     => delay { Option(self.getAnyRefList(path).asScala).map(_.asInstanceOf[T]) }
    case (_, Values.Unknown)    => fail(ConfigException(s"Unable to recognize value for extraction"))
  }

  def subconfig(path: NeString): Task[Conf] = delay {
    new TypesafeConf(self.getConfig(path))
  }
}
