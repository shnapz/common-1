import pulse.plugin._
import pulse.plugin.dependencies._
import local.dependencies._

libraryDependencies ++= Seq(
  typesafe.config,
  "co.fs2" %% "fs2-cats" % "0.3.0",
  refined.core,
  _test(scalatest.core)
)

publishing.settings

local.settings
