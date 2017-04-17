import sbt._
import pulse.plugin._
import bintray.BintrayKeys._
import sbt.Keys._

object local {

  def settings = Seq(
    bintrayOrganization := Some("impulse-io"),
    publishMavenStyle   := true,
    scalacOptions ++= Seq("-feature")
  )


}
