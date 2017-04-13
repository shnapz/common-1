import pulse.plugin._
import pulse.plugin.dependencies._
import local.dependencies._

libraryDependencies ++= Seq(
  fs2.core,
  shapeless.core,
  refined.core,
  apache.lang,
  log4s.core,
  cats.all,
  finagle.core,
  _test(scalatest.core)
)

publishing.settings

local.settings
