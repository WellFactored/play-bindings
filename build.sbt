
name := "play-bindings"

organization := "com.wellfactored"

scalaVersion := "2.11.8"

enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)

git.useGitDescribe in ThisBuild := true

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.4.3" % "provided",
  "com.chuusai" %% "shapeless" % "2.2.5"
)
