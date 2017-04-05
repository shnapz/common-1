package pulse.common.syntax

import com.twitter.util.{Future, Promise}

trait PromiseOps {
  implicit def onComplete[T](p: Promise[T], payload: T, exception: Exception) = {
    if (payload != null) p.setValue(payload) else p.setException(exception)
  }

  implicit def toTwitter[T](body: Promise[T] => Unit) : Future[T] = {
    val p = Promise[T]()
    body(p)
    p
  }
}

import fs2.Strategy


