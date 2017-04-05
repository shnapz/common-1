package pulse.common.syntax

import cats.{Eval, Monad, MonadError}
import com.twitter.util.{Future, Promise}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

trait TwitterFutureImplicits {
  implicit def catsStdInstancesForFuture(implicit ec: ExecutionContext) =
    new Monad[Future] with MonadError[Future, Throwable] {
      def pure[A](x: A): Future[A] = Future.value(x)

      def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)

      /**
        * Note that while this implementation will not compile with `@tailrec`,
        * it is in fact stack-safe.
        */
      final def tailRecM[B, C](b: B)(f: B => Future[Either[B, C]]): Future[C] =
        f(b).flatMap {
          case Left(b1) => tailRecM(b1)(f)
          case Right(c) => Future.value(c)
        }

      def handleErrorWith[A](fea: Future[A])(f: Throwable => Future[A]): Future[A] = fea.rescue { case t => f(t) }

      def raiseError[A](e: Throwable): Future[A] = Future.exception(e)

      override def handleError[A](fea: Future[A])(f: Throwable => A): Future[A] = fea.handle { case t => f(t) }

      override def attempt[A](fa: Future[A]): Future[Either[Throwable, A]] =
        fa.map(a => Right[Throwable, A](a)) handle { case NonFatal(t) => Left(t) }

      override def recover[A](fa: Future[A])(pf: PartialFunction[Throwable, A]): Future[A] = fa.handle(pf)

      override def recoverWith[A](fa: Future[A])(pf: PartialFunction[Throwable, Future[A]]): Future[A] = fa.rescue(pf)

      override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)

      override def catchNonFatal[A](a: => A)(implicit ev: Throwable <:< Throwable): Future[A] = Future(a)

      override def catchNonFatalEval[A](a: Eval[A])(implicit ev: Throwable <:< Throwable): Future[A] = Future(a.value)
    }
}


