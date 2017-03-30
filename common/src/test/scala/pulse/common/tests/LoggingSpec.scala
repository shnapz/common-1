package pulse.common.tests

import org.scalatest.{FlatSpec, ShouldMatchers}

class LoggingSpec extends FlatSpec with ShouldMatchers {

  "throwable descriptor" should "be visible in common logging package" in {
    import pulse.common.logging._

    val t = new Exception("")
    describe(t)
  }
}
