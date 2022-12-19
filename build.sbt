name := "play-pac4j-java-demo"

version := "12.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

val playPac4jVersion = "12.0.0-SNAPSHOT"
val pac4jVersion = "6.0.0-RC5-SNAPSHOT"
val playVersion = "2.8.18"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.google.inject"            % "guice"                % "5.1.0",
  "com.google.inject.extensions" % "guice-assistedinject" % "5.1.0"
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
  "com.typesafe.play" % "play-cache_2.13" % playVersion,
  "ch.qos.logback" % "logback-classic" % "1.4.5",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1",
  "org.projectlombok" % "lombok" % "1.18.24"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/", "Shibboleth releases" at "https://build.shibboleth.net/nexus/content/repositories/releases/")
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/ivy-releases/"

routesGenerator := InjectedRoutesGenerator
