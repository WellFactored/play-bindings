
name := "play-bindings"

organization := "com.wellfactored"

organizationName := "Well-Factored Software Ltd."

startYear := Some(2016)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.12.4", "2.11.8")

licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .enablePlugins(AutomateHeaderPlugin)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)

libraryDependencies += scalaVersion(sv => play(sv)).value

def play(scalaVersion: String) = scalaVersion match {
  case "2.11.8" => "com.typesafe.play" %% "play" % "2.5.18" % Provided
  case _        => "com.typesafe.play" %% "play" % "2.6.12" % Provided
}