name := "play-pac4j-java-demo"

version := "12.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "3.3.4"

val playPac4jVersion = "12.0.2-PLAY3.0"
val pac4jVersion = "6.2.0"
val playVersion = "3.0.8"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.google.inject"            % "guice"                % "6.0.0",
  "com.google.inject.extensions" % "guice-assistedinject" % "6.0.0"
)

libraryDependencies ++= Seq(
  caffeine,
  //ehcache,
  "org.pac4j" %% "play-pac4j" % playPac4jVersion,
  "org.pac4j" % "pac4j-http" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-cas" % pac4jVersion exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.pac4j" % "pac4j-oauth" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-saml" % pac4jVersion excludeAll(ExclusionRule("org.springframework", "spring-core"), ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-oidc" % pac4jVersion excludeAll(ExclusionRule("commons-io", "commons-io"), ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-gae" % pac4jVersion,
  "org.pac4j" % "pac4j-jwt" % pac4jVersion exclude("commons-io", "commons-io"),
  "org.pac4j" % "pac4j-ldap" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-sql" % pac4jVersion exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.pac4j" % "pac4j-mongo" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-kerberos" % pac4jVersion exclude("org.springframework", "spring-core"),
  "org.pac4j" % "pac4j-couch" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.playframework" % "play-cache_3" % playVersion,
  "ch.qos.logback" % "logback-classic" % "1.5.18",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.19.2",
  "org.projectlombok" % "lombok" % "1.18.38",
  "org.springframework" % "spring-context" % "6.2.9"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/", "Shibboleth releases" at "https://build.shibboleth.net/nexus/content/repositories/releases/")
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/ivy-releases/"

routesGenerator := InjectedRoutesGenerator

ThisBuild / evictionErrorLevel := Level.Info
