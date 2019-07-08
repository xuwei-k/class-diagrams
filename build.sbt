name := "class-diagrams"

scalaVersion := "2.13.0"

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Ywarn-unused:imports" ::
  Nil
)

libraryDependencies ++= (
  ("com.chuusai" %% "shapeless" % "2.3.3") ::
  ("org.scalikejdbc" %% "scalikejdbc" % "3.3.5") ::
  ("com.github.scopt" %% "scopt" % "3.7.1") ::
  ("javax.servlet" % "javax.servlet-api" % "3.1.0") ::
  ("org.scalatest" %% "scalatest" % "3.0.8") ::
  ("org.specs2" %% "specs2-core" % "4.6.0") ::
  ("com.typesafe.akka" %% "akka-actor" % "2.5.23") ::
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.scala-lang" % "scalap" % scalaVersion.value) ::
  ("org.scalaz" %% "scalaz-scalacheck-binding" % "7.2.28-scalacheck-1.14") ::
  Nil
)
