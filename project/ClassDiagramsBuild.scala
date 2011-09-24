import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val sourceCount    = TaskKey[Unit]("source-count")
  val createSxrSlide = TaskKey[Unit]("create-sxr-slide")

  val projectName = "class-diagrams"
  val ScalaV = "2.9.1"
  val unfilteredProjects = Seq(
    "filter","agents","uploads","utils","jetty","jetty-ajp","netty-server",
    "netty","json","websockets","oauth","scalate","spec","scalatest"
  ).map{n => "net.databinder" %% ("unfiltered-" + n ) % "0.5.0"}

  lazy val root = Project(projectName, file("."),
    settings = {
      Defaults.defaultSettings ++ 
      sbtappengine.AppenginePlugin.webSettings ++ 
      Seq(
        scalaVersion := ScalaV , 
        libraryDependencies ++= {
          val (gae,gaeSDK) = ("com.google.appengine","1.5.2")
          Seq(
             "javax.servlet" % "servlet-api" % "2.5"
            ,gae % "appengine-java-sdk" % gaeSDK 
            ,gae % "appengine-api-1.0-sdk" % gaeSDK 
            ,"net.kindleit" % "gae-runtime" % gaeSDK 
            ,"org.scalatra" %% "scalatra" % "2.0.0"
            ,"org.jruby" % "jruby" % "1.6.4"
            ,"com.mongodb.casbah" % "casbah-core_2.9.0-1" % "2.2.0-SNAPSHOT"
            ,"org.specs2" %% "specs2" % "1.6.1"
            ,"org.clojure" % "clojure" % "1.3.0-beta2"
            ,"net.lag" % "kestrel" % "2.1.0"
            ,"org.apache.ant" % "ant" % "1.8.2"
            ,"net.lag" % "configgy" % "2.0.2"
            ,"org.codehaus.groovy" % "groovy" % "1.8.2"
            ,"org.scala-lang" % "scala-compiler" % ScalaV
            ,"org.scala-lang" % "scalap" % ScalaV
            ,"org.scala-lang" % "jline" % ScalaV
            ,"org.scalaz" %% "scalaz" % "6.0.3"
            ,"org.scalaj" %% "scalaj-http" % "0.2.9"
            ,"com.foursquare" %% "rogue" % "1.0.24-SNAPSHOT"
            ,"org.scalaxb" %% "scalaxb" % "0.6.4"
          ) ++ unfilteredProjects
        }
        ,resolvers ++= Seq(
            "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
           ,"xuwei-k repo" at "http://xuwei-k.github.com/mvn"
           ,"twitter repo" at "http://maven.twttr.com"
           ,ScalaToolsSnapshots
//         ,"typesafe snapshot" at "http://typesafe.artifactoryonline.com/typesafe/ivy-snapshots/"
         )
        ,addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")
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

