package pulse.config

import java.io.File

/**
 * Represents a configuration source
 */
sealed trait Source extends Product with Serializable

object Source {
  /**
   * Represents a configuration source in the classpath
   */
  case object Classpath extends Source

  /**
   * Represents a configuration source in external file
   *
   * @param path path to the configuration file
   */
  case class FileSource (path: File) extends Source

  /**
   * Raw source. The entire configuration is described as a string source
   *
   * @param source the configuration
   */
  case class Raw (source: String) extends Source
}

