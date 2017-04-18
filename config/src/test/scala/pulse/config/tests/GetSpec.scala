package pulse.config
package tests

import org.scalatest.{FlatSpec, ShouldMatchers}
import eu.timepit.refined.auto._
import fs2.interop.cats._

import readers._
import syntax._
import typesafe._

class GetSpec extends FlatSpec with ShouldMatchers {

  "config reader" should "read all value classes supported by Config" in {

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

  it should "read all reference classes supported by Config" in {

    val testValueA = "testValueA"
    val testValueB = "testValueB"

    val section = for {
      _ <- conf
      a <- get[String]("config.testA")
      b <- get[AnyRef]("config.testB")
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

  it should "read all lists supported by Config" in {

    val testValueStringList = List("hello", "world")
    val testValueAnyRefList = List("any", "ref")
    val testValueIntList = List(120, 130)
    val testValueDoubleList = List(0.250, 0.350)
    val testValueLongList = List(10L, 20L)
    val testValueBooleanList = List(true, false)

    val section = for {
      _ <- conf
      strings <- get[Seq[String]]("config.testStringList")
      anyRefs <- get[Seq[AnyRef]]("config.testAnyRefList")
      ints <- get[Seq[Int]]("config.testIntList")
      doubles <- get[Seq[Double]]("config.testDoubleList")
      longs <- get[Seq[Long]]("config.testLongList")
      bools <- get[Seq[Boolean]]("config.testBooleanList")
    } yield (strings, anyRefs, ints, doubles, longs, bools)

    val config =
      s"""
         |{
         |   config {
         |     testStringList = ${testValueStringList.mkString("[",",","]")}
         |     testAnyRefList = ${testValueAnyRefList.mkString("[",",","]")}
         |     testIntList = ${testValueIntList.mkString("[",",","]")}
         |     testDoubleList = ${testValueDoubleList.mkString("[",",","]")}
         |     testLongList = ${testValueLongList.mkString("[",",","]")}
         |     testBooleanList = ${testValueBooleanList.mkString("[",",","]")}
         |   }
         |}
      """.stripMargin

    (section =<< Conf.immutable(Source.Raw(config))).unsafeRun() match {
      case (strings, anyRefs, ints, doubles, longs, bools) =>
        strings  shouldBe testValueStringList
        anyRefs  shouldBe testValueAnyRefList
        ints  shouldBe testValueIntList
        doubles shouldBe testValueDoubleList
        longs  shouldBe testValueLongList
        bools  shouldBe testValueBooleanList
    }
  }

  it should "correctly process absent values" in {

    val section = for {
      _ <- conf
      a <- get[String]("config.testNone")
    } yield a

    val caught =
      intercept[pulse.common.exceptions.ConfigException] {
        (section =<< Conf.immutable(Source.Raw("config{}"))).unsafeRun()
      }

    caught.getMessage shouldBe "Unable to locate path 'config.testNone' in the configuration"
  }
}
