import sbt._

object local {
  object dependencies {

    object versions {
      val log4s = "1.3.4"
    }

    object log4s {
      val core = "org.log4s" %% "log4s" % versions.log4s
    }
  }
}