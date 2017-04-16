import pulse.plugin._
import pulse.plugin.dependencies._

libraryDependencies ++= Seq(
  typesafe.config,
  fs2.cats,
  refined.core,
  _test(scalatest.core),
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)

publishing.settings

local.settings
