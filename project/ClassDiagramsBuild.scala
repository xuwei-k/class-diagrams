import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val root = Project("class-diagrams", file(".")).settings(
    sbtappengine.Plugin.appengineSettings: _*
  ).settings(
    scalaVersion := "2.11.2",
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
    ),
    libraryDependencies ++= (
      ("org.eclipse.jetty" % "jetty-webapp" % "7.6.15.v20140411" % "container") ::
      ("com.chuusai" %% "shapeless" % "2.0.0") ::
      ("org.scala-lang.modules" %% "scala-async" % "0.9.2") ::
      ("org.scalikejdbc" %% "scalikejdbc" % "2.0.7") ::
      ("com.github.scopt" %% "scopt" % "3.2.0") ::
      ("javax.servlet" % "servlet-api" % "2.5") ::
      ("org.scalatest" %% "scalatest" % "2.2.0") ::
//      ("org.specs2" %% "specs2" % "2.3.12") ::
      ("com.typesafe.akka" %% "akka-actor" % "2.3.4") ::
      ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
      ("org.scala-lang" % "scalap" % scalaVersion.value) ::
      ("org.scalaz" %% "scalaz-scalacheck-binding" % "7.1.0-RC2") ::
      ("org.json4s" %% "json4s-native" % "3.2.10") ::
      ("net.debasishg" %% "redisclient" % "2.13") ::
      Nil
    ).map(_.excludeAll(
      ExclusionRule(organization = "com.sun.jdmk"),
      ExclusionRule(organization = "com.sun.jmx"),
      ExclusionRule(organization = "javax.jms")
    ))
  )

}

