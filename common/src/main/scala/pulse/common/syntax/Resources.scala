package pulse.common.syntax

import cats.{Monad, MonadError}
import pulse.common.Managed

trait Resources {

  def use[F[_], A,B](instance: A)(body: A => F[B])(implicit M: Monad[F], R: Managed[A], E: MonadError[F, Throwable]) = M.flatMap(E.attempt(body(instance))) {
    case Left (failure) => R.close(instance); E.raiseError[B](failure)
    case Right(content) => R.close(instance); M.pure(content)
  }

  def bracket[F[_], A, B](in: F[A])(b: A => F[B])(implicit M: Monad[F], R: Managed[A], E: MonadError[F, Throwable]) =
    M.flatMap(E.attempt(in)) {
      case Left (f) => E.raiseError[B](f)
      case Right(c) => use(c)(b)
    }


}
