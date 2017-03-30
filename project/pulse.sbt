resolvers += Resolver.url("impulse bintray", url("https://dl.bintray.com/impulse-io/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("impulse-io" % "sbt-pulse" % "1.0.7")
