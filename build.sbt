
name := "play-bindings"

organization := "com.wellfactored"

organizationName := "Well-Factored Software Ltd."

startYear := Some(2016)

scalaVersion := "2.12.4"

licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .enablePlugins(AutomateHeaderPlugin)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.typesafe.play" %% "play" % "2.6.12" % Provided,

  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)

