import pulse.plugin._
import pulse.plugin.dependencies._

libraryDependencies ++= Seq(
  fs2.core,
  shapeless.core,
  refined.core,
  apache.lang,
  log4s.core,
  cats.all,
  fs2.cats,
  finagle.core,
  _test(scalatest.core)
)

settings.common

publishing.settings

local.settings
