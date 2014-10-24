name := "play-pac4j-java-demo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.pac4j" % "play-pac4j_java" % "1.2.2",
  "org.pac4j" % "pac4j-http" % "1.6.0",
  "org.pac4j" % "pac4j-cas" % "1.6.0",
  "org.pac4j" % "pac4j-openid" % "1.6.0",
  "org.pac4j" % "pac4j-oauth" % "1.6.0",
  "org.pac4j" % "pac4j-saml" % "1.6.0"
)

resolvers ++= Seq(
    "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

play.Project.playJavaSettings

