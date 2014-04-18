import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val root = Project("class-diagrams", file(".")).settings(
    sbtappengine.Plugin.appengineSettings: _*
  ).settings(
    scalaVersion := "2.11.0",
    scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked", "-language:_"),
    libraryDependencies ++= Seq(
       "org.eclipse.jetty" % "jetty-webapp" % "7.6.8.v20121106" % "container"
      ,"javax.servlet" % "servlet-api" % "2.5"
      ,"org.specs2" %% "specs2" % "2.3.11"
      ,"com.typesafe.akka" %% "akka-actor" % "2.3.2"
      ,"org.scala-lang" % "scala-compiler" % scalaVersion.value
      ,"org.scala-lang" % "scalap" % scalaVersion.value
      ,"org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.6"
    ).map(_.excludeAll(
      ExclusionRule(organization = "com.sun.jdmk"),
      ExclusionRule(organization = "com.sun.jmx"),
      ExclusionRule(organization = "javax.jms")
    ))
  )

}

