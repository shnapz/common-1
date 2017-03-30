import pulse.plugin._

organization in Global := "impulse-io"

scalaVersion in Global := "2.11.8"

lazy val pulse_common = project.in(file(".")).aggregate(common)

lazy val common   = project

settings.common

publishing.ignore

