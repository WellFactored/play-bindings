publishMavenStyle in ThisBuild := true

useGpg := true

usePgpKeyHex("46C41F3C")

publishTo := sonatypePublishTo.value

pomIncludeRepository in ThisBuild := { _ => false }

homepage in ThisBuild := Some(url(s"https://github.com/wellfactored/${name.value}"))

scmInfo in ThisBuild := Some(ScmInfo(url(s"http://github.com/wellfactored/${name.value}"), s"scm:git@github.com:wellfactored/${name.value}.git"))

pomExtra in ThisBuild :=
  <developers>
    <developer>
      <id>dclinton</id>
      <name>Doug Clinton</name>
    </developer>
  </developers>

