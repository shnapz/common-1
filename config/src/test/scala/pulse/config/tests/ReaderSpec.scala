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

  it should "read all value classes supported by Config" in {

    val testValueInt = 120
    val testValueDouble = 0.250
    val testValueLong = 10L
    val testValueBoolean = true

    val section = for {
      _ <- conf
      int <- get[Int]("config.testInt")
      double <- get[Double]("config.testDouble")
      long <- get[Long]("config.testLong")
      bool <- get[Boolean]("config.testBoolean")
    } yield (int, double, long, bool)

    val config =
      s"""
         |{
         |   config {
         |     testInt = $testValueInt
         |     testDouble = $testValueDouble
         |     testLong = $testValueLong
         |     testBoolean = $testValueBoolean
         |   }
         |}
      """.stripMargin

    (section =<< Conf.immutable(Source.Raw(config))).unsafeRun() match {
      case (int, double, long, bool) =>
        int  shouldBe testValueInt
        double shouldBe testValueDouble
        long  shouldBe testValueLong
        bool  shouldBe testValueBoolean
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
