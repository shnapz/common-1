package pulse.common.exceptions

case class NotSupportedException(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
