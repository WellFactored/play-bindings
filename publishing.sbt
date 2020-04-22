publishMavenStyle in ThisBuild := true

// There seems to be a problem with this setting. Without it `publishSigned` will work, but get
// an exception about not being able to find the key if I use it.
//pgpSigningKey := Some(0x2F18A34746C41F3CL)

pgpPublicRing := file("/Users/dec/.sonatype/pubring.gpg")
pgpSecretRing := file("/Users/dec/.sonatype/secring.gpg")

publishTo := sonatypePublishTo.value
publishConfiguration := publishConfiguration.value.withOverwrite(true)

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

