package pulse.common.logging

import scala.compat.Platform.EOL

trait DefaultDescriptors {
  import cats._
  import cats.implicits._

  implicit val exceptionShow: Show[Exception] = new Show[Exception] {
    override def show(f: Exception): String = s"{ exceptionMessage=${f.getMessage}, stackTrace=${f.getStackTrace.mkString("", EOL, EOL)} }"
  }

  implicit val throwableShow: Show[Throwable] = exceptionShow.contramap(_.asInstanceOf[Exception])
}
