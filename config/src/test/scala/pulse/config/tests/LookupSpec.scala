package pulse.config
package tests

import cats.data.Kleisli
import org.scalatest.{FlatSpec, ShouldMatchers}
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import readers._
import syntax._
import typesafe._

class LookupSpec extends FlatSpec with ShouldMatchers {

  "config reader" should "read all value classes supported by Config" in {

    val testValueInt = 120
    val testValueDouble = 0.250
    val testValueLong = 10L
    val testValueBoolean = true

    val section = for {
      _ <- conf
      int <- lookup[Int]("config.testInt")
      double <- lookup[Double]("config.testDouble")
      long <- lookup[Long]("config.testLong")
      bool <- lookup[Boolean]("config.testBoolean")
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
        int  shouldBe Some(testValueInt)
        double shouldBe Some(testValueDouble)
        long  shouldBe Some(testValueLong)
        bool  shouldBe Some(testValueBoolean)
    }
  }

  it should "read all reference classes supported by Config" in {

    val testValueA = "testValueA"
    val testValueB = "testValueB"

    val section = for {
      _ <- conf
      a <- lookup[String]("config.testA")
      b <- lookup[AnyRef]("config.testB")
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
        left  shouldBe Some(testValueA)
        right shouldBe Some(testValueB)
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
      strings <- lookup[Seq[String]]("config.testStringList")
      anyRefs <- lookup[Seq[AnyRef]]("config.testAnyRefList")
      ints <- lookup[Seq[Int]]("config.testIntList")
      doubles <- lookup[Seq[Double]]("config.testDoubleList")
      longs <- lookup[Seq[Long]]("config.testLongList")
      bools <- lookup[Seq[Boolean]]("config.testBooleanList")
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
        strings  shouldBe Some(testValueStringList)
        anyRefs  shouldBe Some(testValueAnyRefList)
        ints  shouldBe Some(testValueIntList)
        doubles shouldBe Some(testValueDoubleList)
        longs  shouldBe Some(testValueLongList)
        bools  shouldBe Some(testValueBooleanList)
    }
  }

  it should "correctly process absent values" in {

    val section = for {
      _ <- conf
      a <- lookup[String]("config.testNone")
    } yield a

    (section =<< Conf.immutable(Source.Raw("config{}"))).unsafeRun() shouldBe None
  }
}
