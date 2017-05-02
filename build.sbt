name := "play-pac4j-java-demo"

version := "3.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.pac4j" % "play-pac4j" % "3.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-http" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-cas" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-openid" % "2.0.0-RC3-SNAPSHOT" exclude("xml-apis" , "xml-apis"),
  "org.pac4j" % "pac4j-oauth" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-saml" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-oidc" % "2.0.0-RC3-SNAPSHOT" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-gae" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-jwt" % "2.0.0-RC3-SNAPSHOT" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-ldap" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-sql" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-mongo" % "2.0.0-RC3-SNAPSHOT",
  "org.pac4j" % "pac4j-stormpath" % "2.0.0-RC3-SNAPSHOT",
  "com.typesafe.play" % "play-cache_2.11" % "2.5.8",
  "commons-io" % "commons-io" % "2.4",
  "be.objectify" %% "deadbolt-java" % "2.5.1"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

routesGenerator := InjectedRoutesGenerator
