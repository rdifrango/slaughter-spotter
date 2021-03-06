name := "slaughter-spotter"

version := "1.2.0"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-can" % "1.2.0",
  "io.spray" % "spray-http" % "1.2.0",
  "io.spray" % "spray-httpx" % "1.2.0",
  "io.spray" % "spray-routing" % "1.2.0",
  "io.spray" % "spray-servlet" % "1.2.0",
  "io.spray" % "spray-testkit" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3",
  "org.json4s"    %% "json4s-native"   % "3.2.4",
  "org.json4s" % "json4s-ext_2.10" % "3.2.4",
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "c3p0" % "c3p0" % "0.9.1.2",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "ch.qos.logback" % "logback-core" % "1.0.13",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.13.v20130916" % "container",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container" artifacts Artifact("javax.servlet", "jar", "jar"),
  "org.specs2" %% "specs2" % "1.14" % "test"
)

seq(webSettings: _*)