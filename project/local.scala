import sbt._
import pulse.plugin._
import bintray.BintrayKeys._
import sbt.Keys._

object local {
  object dependencies {

    object versions {
      val log4s   = "1.3.4"
      val config  = "1.3.1"
      val refined = "0.8.0"
    }

    object log4s {
      val core = "org.log4s" %% "log4s" % versions.log4s
    }

    object typesafe {
      val config = "com.typesafe" % "config" % versions.config
    }

    object refined {
      val core = "eu.timepit" %% "refined" % versions.refined
    }
  }

  def settings = Seq(
    bintrayOrganization := Some("impulse-io"),
    publishMavenStyle := true
  )
}
