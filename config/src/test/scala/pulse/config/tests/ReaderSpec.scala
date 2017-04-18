package pulse.config
package tests

import org.scalatest.{FlatSpec, ShouldMatchers}
import eu.timepit.refined.auto._
import fs2.interop.cats._

import readers._
import syntax._
import typesafe._

class ReaderSpec extends FlatSpec with ShouldMatchers {

  "config reader" should "perform a basic extraction" in {

    val testValueA = "testValueA"
    val testValueB = "testValueB"

    val section = for {
      _ <- conf
      a <- get[String]("config.testA")
      b <- get[String]("config.testB")
    } yield a -> b

    val config =
      s"""
      |{
      |   config {
      |     testA = "$testValueA"
      |     testB = "$testValueB"
      |   }
      |}
      """.stripMargin

    (section =<< Conf.immutable(Source.Raw(config))).unsafeRun() match {
      case (left, right) =>
        left  shouldBe testValueA
        right shouldBe testValueB
    }
  }

  it should "pick up nested sections" in {

    val testValueA = "testValueA"
    val testValueB = "testValueB"
    val testValueC = "testValueC"

    val section = for {
      _ <- conf
      a <- get[String]("config.testA")
      b <- get[String]("config.testB")
      s <- subconfig("config.subconfig")
      c <- s.get[String]("testC")
    } yield (a,b,c)

    val config =
      s"""
         |{
         |   config {
         |     testA = "$testValueA"
         |     testB = "$testValueB"
         |
         |     subconfig {
         |      testC = "$testValueC"
         |     }
         |   }
         |}
      """.stripMargin

    (section =<< Conf.immutable(Source.Raw(config))).unsafeRun() match {
      case (_a, b, c) =>
        _a shouldBe testValueA
        b  shouldBe testValueB
        c  shouldBe testValueC
    }
  }
}
