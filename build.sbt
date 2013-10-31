name := "slaughter-spotter"

version := "1.0"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-can" % "1.1-M8",
  "io.spray" % "spray-http" % "1.1-M8",
  "io.spray" % "spray-httpx" % "1.1-M8",
  "io.spray" % "spray-routing" % "1.1-M8",
  "io.spray" % "spray-servlet" % "1.1-M8",
  "io.spray" % "spray-testkit" % "1.1-M8",
  "com.typesafe.akka" %% "akka-actor" % "2.1.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.1.4",
  "org.json4s"    %% "json4s-native"   % "3.2.4",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.13.v20130916" % "container",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container" artifacts Artifact("javax.servlet", "jar", "jar"),
  "org.specs2" %% "specs2" % "1.14" % "test"
)

seq(webSettings: _*)