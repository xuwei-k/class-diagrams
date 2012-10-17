import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val sourceCount    = TaskKey[Unit]("source-count")
  val createSxrSlide = TaskKey[Unit]("create-sxr-slide")

  val projectName = "class-diagrams"
  val ScalaV = "2.9.1"
  val unfilteredProjects = Seq(
    "filter","filter-async","agents","uploads","util","jetty","jetty-ajp","netty-server",
    "netty","json","netty-websockets","oauth","spec","scalatest"
  ).map{n => "net.databinder" %% ("unfiltered-" + n ) % "0.6.4"}

  val liftProjects = Seq(
    "common","json","actor","util","json-scalaz","json-ext"
  ).map{n => "net.liftweb" %% ("lift-" + n ) % "2.4"}

  val mirah = Seq("mirah","mirah-complete").map{"org.mirah" % _ % "0.0.7"}

  // TODO util-thrift dependencies unresolve :(
  val twitterUtils = Seq(
    "codec","zk","logging","reflect","zk-common","core","hashing","eval","collection"
  ).map{n => "com.twitter" %% ("util-"+n) % "3.0.0" }

  lazy val root = Project(projectName, file("."),
    settings = {
      Defaults.defaultSettings ++
      sbtappengine.Plugin.webSettings ++
      Seq(
        scalaVersion := ScalaV ,
        libraryDependencies <++= sbtDependency{ sd =>
          {Seq(
             sd
            ,"org.eclipse.jetty" % "jetty-webapp" % "7.4.5.v20110725" % "container"
            ,"javax.servlet" % "servlet-api" % "2.5"
            ,"org.scalatra" %% "scalatra" % "2.0.4"
            ,"org.jruby" % "jruby" % "1.6.5"
            ,"com.mongodb.casbah" %% "casbah-core" % "2.1.5-1"
            ,"org.specs2" %% "specs2" % "1.12.2"
            ,"org.clojure" % "clojure" % "1.3.0"
//            ,"org.codehaus.groovy" % "groovy" % "1.8.2"
            ,"org.scala-lang" % "scala-compiler" % ScalaV
            ,"org.scala-lang" % "scalap" % ScalaV
            ,"org.scala-lang" % "jline" % ScalaV
            ,"org.scalaz" %% "scalaz-full" % "6.0.4"
            ,"org.scalaj" %% "scalaj-http" % "0.3.2"
            ,"com.foursquare" %% "rogue" % "1.1.8"
            ,"org.scalaxb" %% "scalaxb" % "0.7.3"
            ,"com.codecommit" %% "anti-xml" % "0.3"
            ,"org.scala-tools" %% "scala-stm" % "0.6"
            ,"com.github.okomok" % "sing_2.9.0" % "0.1.0"
            ,"com.github.okomok" %% "ken" % "0.1.0"
            ,"com.google.android" % "android" % "2.1.2"
            ,"play" %% "play" % "2.0.2"
            ,"com.m3" % "scalaflavor4j" % "1.0.3"
          ) ++ unfilteredProjects ++ liftProjects ++ twitterUtils // ++ mirah
          }.map{_.excludeAll(
            ExclusionRule(organization = "com.sun.jdmk"),
            ExclusionRule(organization = "com.sun.jmx"),
            ExclusionRule(organization = "javax.jms")
          )}
        }
        ,resolvers ++= Seq(
            "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
           ,"xuwei-k repo" at "http://xuwei-k.github.com/mvn"
           ,"twitter repo" at "http://maven.twttr.com"
           ,"guicefruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
           ,"okomok releases" at "http://okomok.github.com/maven-repo/releases"
           ,"typesafe" at "http://repo.typesafe.com/typesafe/releases"
           ,"akka" at "http://akka.io/repository"
/*
           ,Resolver.url(
             "typesafe ivy release",
             new URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases")
           )(Resolver.ivyStylePatterns)
           ,"Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
           ,"typesafe snapshot" at "http://typesafe.artifactoryonline.com/typesafe/ivy-snapshots/"
*/
         )
        ,addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")
        ,scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }
        ,sourceCount <<= ( sources in Compile , sources in Test ) map{ (main,test) =>
           println{
             "\nmain " + main.map{f => IO.readLines(f).size}.sum +
             "\ntest " + test.map{f => IO.readLines(f).size}.sum
           }
        }
        ,createSxrSlide <<= ( sources in Compile , sources in Test ) map{ (main,test) =>
          Seq(main -> "main" ,test -> "test" ).foreach{ case (files,n) =>
            IO.write( file("../slide") / n , create(SxrBaseURL + n + "/",files.map{_.getName}) )
          }
        }
      )
    }
  )

  val SxrBaseURL = "http://xuwei-k.github.com/" + projectName + "/"

  def dom(src:String,w:Int,h:Int):xml.Elem = {
    <iframe src={src} width={w.toString} height={h.toString} />
  }

  def create(baseURL:String,files:Seq[String],w:Int = 1200,h:Int = 600):String = {
    files.map{ f =>
      "\n\n!SLIDE\n\n" + dom( baseURL + f , w , h )
    }.mkString
  }
}

