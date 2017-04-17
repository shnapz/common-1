import pulse.plugin._
import pulse.plugin.dependencies._

libraryDependencies ++= Seq(
  typesafe.config,
  fs2.cats,
  refined.core,
  _test(scalatest.core)
)

settings.common

publishing.settings

local.settings
