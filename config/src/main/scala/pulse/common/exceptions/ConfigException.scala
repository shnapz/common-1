package pulse.common.exceptions

case class ConfigException(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
