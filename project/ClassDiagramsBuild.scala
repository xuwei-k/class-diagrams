import sbt._
import Keys._

object ClassDiagramBuild extends Build{

  val sourceCount    = TaskKey[Unit]("source-count")
  val createSxrSlide = TaskKey[Unit]("create-sxr-slide")

  val projectName = "class-diagrams"

  lazy val root = Project(projectName, file("."),
    settings = {
      Defaults.defaultSettings ++ 
      sbtappengine.AppenginePlugin.webSettings ++ 
      Seq(
        scalaVersion := "2.9.1", 
        libraryDependencies ++= {
          val (gae,gaeSDK) = ("com.google.appengine","1.5.2")
          Seq(
             "javax.servlet" % "servlet-api" % "2.5"
            ,gae % "appengine-java-sdk" % gaeSDK 
            ,gae % "appengine-api-1.0-sdk" % gaeSDK 
            ,"net.kindleit" % "gae-runtime" % gaeSDK 
            ,"org.scalatra" %% "scalatra" % "2.0.0"
            ,"com.mongodb.casbah" % "casbah-core_2.9.0-1" % "2.2.0-SNAPSHOT"
            ,"org.specs2" %% "specs2" % "1.6.1"
            ,"org.clojure" % "clojure" % "1.3.0-beta2"
          )
        }
        ,resolvers ++= Seq(
            "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
           ,"xuwei-k repo" at "http://xuwei-k.github.com/mvn"
           ,ScalaToolsSnapshots
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

