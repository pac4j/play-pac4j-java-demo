name := "play-pac4j-java-demo"

version := "2.6.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,DebianPlugin)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  javaWs,
  evolutions,
  "org.pac4j" % "play-pac4j" % "2.6.2",
  "org.pac4j" % "pac4j-http" % "1.9.5",
  "org.pac4j" % "pac4j-cas" % "1.9.5",
  "org.pac4j" % "pac4j-openid" % "1.9.5" exclude("xml-apis" , "xml-apis"),
  "org.pac4j" % "pac4j-oauth" % "1.9.5",
  "org.pac4j" % "pac4j-saml" % "1.9.5",
  "org.pac4j" % "pac4j-oidc" % "1.9.5" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-gae" % "1.9.5",
  "org.pac4j" % "pac4j-jwt" % "1.9.5" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-ldap" % "1.9.5",
  "org.pac4j" % "pac4j-sql" % "1.9.5",
  "org.pac4j" % "pac4j-mongo" % "1.9.5",
  "org.pac4j" % "pac4j-stormpath" % "1.9.5",
  "com.typesafe.play" % "play-cache_2.11" % "2.5.4",
  "commons-io" % "commons-io" % "2.4",
  "be.objectify" %% "deadbolt-java" % "2.5.1"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

routesGenerator := InjectedRoutesGenerator
