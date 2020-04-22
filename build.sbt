name := "play-bindings"

organization := "com.wellfactored"

organizationName := "Well-Factored Software Ltd."

startYear := Some(2016)

val ScalaVersion2_11 = "2.11.8"
val ScalaVersion2_12 = "2.12.10"
val ScalaVersion2_13 = "2.13.1"

scalaVersion := ScalaVersion2_13

crossScalaVersions := Seq(ScalaVersion2_13, ScalaVersion2_12, ScalaVersion2_11)

licenses += ("Apache-2.0", new URL(
  "https://www.apache.org/licenses/LICENSE-2.0.txt"
))

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .enablePlugins(AutomateHeaderPlugin)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

libraryDependencies += scalaVersion(sv => play(sv)).value

def play(scalaVersion: String) = scalaVersion match {
  case ScalaVersion2_11 => "com.typesafe.play" %% "play" % "2.5.18" % Provided
  case ScalaVersion2_12 => "com.typesafe.play" %% "play" % "2.7.4" % Provided
  case _                => "com.typesafe.play" %% "play" % "2.8.1" % Provided
}
