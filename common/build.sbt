import pulse.plugin._
import pulse.plugin.dependencies._
import local.dependencies._

libraryDependencies ++= Seq(
  fs2.core,
  shapeless.core,
  apache.lang,
  log4s.core,
  cats.all,
  _test(scalatest.core)
)

publishing.settings

local.settings
