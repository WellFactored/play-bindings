
name := "play-bindings"

organization := "com.wellfactored"

scalaVersion := "2.11.8"

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.typelevel" %% "cats-core" % "0.8.1",
  "com.typesafe.play" %% "play" % "2.5.9" % Provided,

  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)
