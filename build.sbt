
name := "play-bindings"

organization := "com.wellfactored"

scalaVersion := "2.12.4"

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.typesafe.play" %% "play" % "2.6.12" % Provided,

  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)
