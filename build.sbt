name := "class-diagrams"

scalaVersion := "2.12.2"

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

libraryDependencies ++= (
  ("com.chuusai" %% "shapeless" % "2.3.2") ::
  ("org.scalikejdbc" %% "scalikejdbc" % "3.0.1") ::
  ("com.github.scopt" %% "scopt" % "3.6.0") ::
  ("javax.servlet" % "javax.servlet-api" % "3.1.0") ::
  ("org.scalatest" %% "scalatest" % "3.0.3") ::
  ("org.specs2" %% "specs2-core" % "3.9.1") ::
  ("com.typesafe.akka" %% "akka-actor" % "2.5.3") ::
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.scala-lang" % "scalap" % scalaVersion.value) ::
  ("org.scalaz" %% "scalaz-scalacheck-binding" % "7.2.14") ::
  Nil
)
