name := "play-pac4j-java-demo"

version := "14.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "3.9.0"

val playPac4jVersion = "14.0.0-SNAPSHOT"
val pac4jVersion = "6.5.9"
val playVersion = "3.0.12"

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
  "org.pac4j" % "pac4j-jwt" % pac4jVersion exclude("commons-io", "commons-io"),
  "org.pac4j" % "pac4j-ldap" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-sql" % pac4jVersion exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.pac4j" % "pac4j-mongo" % pac4jVersion excludeAll (ExclusionRule(organization = "com.fasterxml.jackson.core")),
  "org.pac4j" % "pac4j-kerberos" % pac4jVersion exclude("org.springframework", "spring-core"),
  "org.playframework" % "play-cache_3" % playVersion,
  "ch.qos.logback" % "logback-classic" % "1.5.32",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.22.3.1",
  "org.projectlombok" % "lombok" % "1.18.46",
  "org.springframework" % "spring-context" % "7.0.7"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
"Shibboleth releases" at "https://build.shibboleth.net/nexus/content/repositories/releases/",
"Central Portal Snapshots" at "https://central.sonatype.com/repository/maven-snapshots/")
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/ivy-releases/"

routesGenerator := InjectedRoutesGenerator

// JDK 23+ disables implicit annotation processing (needed by Lombok);
// -proc:full is not supported by older JDKs (e.g. 17.0.4)
javacOptions ++= {
  if (sys.props("java.specification.version").toInt >= 23) Seq("-proc:full") else Seq.empty
}

ThisBuild / evictionErrorLevel := Level.Info
