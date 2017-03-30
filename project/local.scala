import sbt._
import pulse.plugin._
import bintray.BintrayKeys._
import sbt.Keys._

object local {
  object dependencies {

    object versions {
      val log4s = "1.3.4"
    }

    object log4s {
      val core = "org.log4s" %% "log4s" % versions.log4s
    }
  }

  def settings = Seq(
    bintrayOrganization := Some("impulse-io"),
    publishMavenStyle := true
  )
}
