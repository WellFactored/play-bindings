
name := "play-bindings"

organization := "com.wellfactored"

crossScalaVersions := Seq("2.10.6", "2.11.8")

scalaVersion := "2.11.8"

lazy val `play-bindings` =
  (project in file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.wellfactored" %% "value-wrapper" % "1.2.0",
  "com.typesafe.play" %% "play" % "2.3.9" % Provided,
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),

  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)
