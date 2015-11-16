
import play.core.PlayVersion

resolvers := Seq(
  Resolver.bintrayRepo("hmrc", "releases"),
  "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
)

name := "play-extras"

organization := "com.wellfactored"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % PlayVersion.current % "provided"
)
