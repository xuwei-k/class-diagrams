package xuwei_k.classDiagram

object URLMap{

  @inline final val GITHUB = "https://github.com/"
  @inline final val SBT_SXR = "http://harrah.github.com/xsbt/latest/sxr/"
  @inline final val GITHUB_SCALA_2_9_1 = GITHUB + "scala/scala/blob/v2.9.1/src/"
  @inline final val LINE1 = ".scala#L1"

  def apply(name: String):String = {
    val fullName = name.split("""\$""").head
    val path = fullName.replace(".", "/")

    def s(prefix:String) = fullName.startsWith(prefix)
    // TODO use PartialFunction ?
    if(Seq("ant","cmd","nsc","reflect","util").exists{ p =>s("scala.tools." + p ) }){
      GITHUB_SCALA_2_9_1 + "compiler/" + path + LINE1
    } else if (s("scala.tools.scalap") ){
      GITHUB_SCALA_2_9_1 + "scalap/" + path + LINE1
    } else if (s("scala.actors")) {
       GITHUB_SCALA_2_9_1 + "actors/" + path + LINE1
    } else if (s("scala.")) {
       GITHUB_SCALA_2_9_1 + "library/" + path + LINE1
    } else if (s("java")) {
      "http://java.sun.com/javase/6/docs/api/" + path + ".html"
    } else if (s("org.jruby")){
      "http://www.jruby.org/apidocs/" + path + ".html"
    } else if (s("groovy")){
      "http://groovy.codehaus.org/api/" + path + ".html"
    } else if (s("scalaz.")){
      "http://scalaz.github.com/scalaz/scalaz-2.9.1-6.0.4/doc.sxr/" + path + ".scala"
    } else if (s("xsbt")){
      SBT_SXR + path + ".java"
    } else if (s("sbt.")){
      SBT_SXR + path.split("/").last + ".scala"
    } else if (s("akka.")){
      "http://akka.io/api/akka/1.2/#" + fullName
    } else if (s("com.mongodb.casbah")){
      "http://api.mongodb.org/scala/casbah/2.1.5.0/scaladoc/#" + fullName
    } else if (s("specs2.") || s("org.specs2.")){
      "http://etorreborre.github.com/specs2/api/#" + fullName
    } else if (s("net.liftweb.")){
      "http://scala-tools.org/mvnsites/liftweb-2.4/#"+ fullName
    } else if (s("android.")){
      "http://developer.android.com/reference/" + path.replace('$','.') + ".html"
    } else if (s("play.")){
      GITHUB + "playframework/Play20/tree/2.0/framework/src/play/src/main/scala/" + path + LINE1
    } else if (s("org.scalatra.")){
      GITHUB + "scalatra/scalatra/blob/2.0.4/core/src/main/scala/" + path + LINE1
    } else if (s("clojure.lang")){
      GITHUB + "clojure/clojure/blob/clojure-1.3.0/src/jvm/" + path + ".java#L1"
    } else if (s("com.github.okomok.ken")){
      GITHUB + "okomok/ken/blob/0.1.0/src/main/scala/" + path + ".scala#L1"
    } else if (s("com.github.okomok.sing")){
      GITHUB + "okomok/sing/blob/0.1.0/src/main/scala/" + path.replace("com/github/okomok/sing/","") + ".scala#L1"
    } else ""
  }
}

