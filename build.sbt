
name := "play-bindings"

organization := "com.wellfactored"

scalaVersion := "2.11.8"

enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.4.3",
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.typelevel" %% "cats-core" % "0.6.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
