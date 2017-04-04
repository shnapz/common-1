package pulse.common.tests

import cats.{Monad, MonadError}
import org.scalatest.{FlatSpec, ShouldMatchers}
import pulse.common.{Managed, exceptions}
import pulse.common.exceptions.NotSupportedException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ResourcesSpec extends FlatSpec with ShouldMatchers {

  class Resource(var closed: Boolean)

  implicit val futureMonadError = new MonadError[Future, Throwable] {

    override def raiseError[A](e: Throwable): Future[A] = Future.failed(e)

    override def handleErrorWith[A](fa: Future[A])(f: (Throwable) => Future[A]): Future[A] = fa.recoverWith {
      case t => f(t)
    }

    override def pure[A](x: A): Future[A] = Future.successful(x)

    override def flatMap[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa flatMap f

    override def tailRecM[A, B](a: A)(f: (A) => Future[Either[A, B]]): Future[B] = flatMap(f(a)) {
      case Right(item) => Future.successful(item)
      case Left (fail) => tailRecM(fail)(f)
    }
  }

  implicit val managedResource = new Managed[Resource] {
    override def close(instance: Resource): Unit = instance.closed = true
  }

  implicit val futureMonad = new Monad[Future] {

    override def pure[A](x: A): Future[A] = Future.successful(x)

    override def flatMap[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa flatMap f

    override def tailRecM[A, B](a: A)(f: (A) => Future[Either[A, B]]): Future[B] = flatMap(f(a)) {
      case Right(item) => Future.successful(item)
      case Left (fail) => tailRecM(fail)(f)
    }
  }

  "common syntax" should "allow release resources in monadic expressions" in {
    import pulse.common.syntax._

    val resource = new Resource(false)

    val positive = use(resource) {
      r => Future(r)
    }

    resource.closed = false

    val negative = use(resource) {
      r => Future.failed(NotSupportedException("n/a"))
    }

    Await.result(positive, 1.second).closed shouldBe true
    a[NotSupportedException] shouldBe thrownBy {
      Await.result(negative, 1.second)
    }
    resource.closed shouldBe true
  }
}
