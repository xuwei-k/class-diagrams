name := "class-diagrams"

scalaVersion := "2.11.6"

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= (
  ("org.msgpack" % "msgpack-core" % "0.7.0-p7") ::
  ("com.chuusai" %% "shapeless" % "2.1.0") ::
  ("org.scala-lang.modules" %% "scala-async" % "0.9.3") ::
  ("org.scalikejdbc" %% "scalikejdbc" % "2.2.4") ::
  ("com.github.scopt" %% "scopt" % "3.3.0") ::
  ("javax.servlet" % "servlet-api" % "2.5") ::
  ("org.scalatest" %% "scalatest" % "2.2.4") ::
  ("org.specs2" %% "specs2" % "3.0") ::
  ("com.typesafe.akka" %% "akka-actor" % "2.3.9") ::
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.scala-lang" % "scalap" % scalaVersion.value) ::
  ("org.scalaz" %% "scalaz-scalacheck-binding" % "7.1.2") ::
  ("org.json4s" %% "json4s-native" % "3.2.11") ::
  ("net.debasishg" %% "redisclient" % "2.14") ::
  Nil
).map(_.excludeAll(
  ExclusionRule(organization = "com.sun.jdmk"),
  ExclusionRule(organization = "com.sun.jmx"),
  ExclusionRule(organization = "javax.jms")
))
