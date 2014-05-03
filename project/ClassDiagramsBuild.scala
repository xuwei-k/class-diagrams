import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val root = Project("class-diagrams", file(".")).settings(
    sbtappengine.Plugin.appengineSettings: _*
  ).settings(
    scalaVersion := "2.11.0",
    scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked", "-language:_"),
    libraryDependencies ++= (
      ("org.eclipse.jetty" % "jetty-webapp" % "7.6.15.v20140411" % "container") ::
      ("com.chuusai" %% "shapeless" % "2.0.0") ::
      ("org.scala-lang.modules" %% "scala-async" % "0.9.1") ::
      ("org.scalikejdbc" %% "scalikejdbc" % "2.0.0-beta3") ::
      ("com.github.scopt" %% "scopt" % "3.2.0") ::
      ("javax.servlet" % "servlet-api" % "2.5") ::
      ("org.scalatest" %% "scalatest" % "2.1.5") ::
      ("org.specs2" %% "specs2" % "2.3.11") ::
      ("com.typesafe.akka" %% "akka-actor" % "2.3.2") ::
      ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
      ("org.scala-lang" % "scalap" % scalaVersion.value) ::
      ("org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.6") ::
      ("org.json4s" %% "json4s-native" % "3.2.9") ::
      ("net.debasishg" %% "redisclient" % "2.13") ::
      Nil
    ).map(_.excludeAll(
      ExclusionRule(organization = "com.sun.jdmk"),
      ExclusionRule(organization = "com.sun.jmx"),
      ExclusionRule(organization = "javax.jms")
    ))
  )

}

