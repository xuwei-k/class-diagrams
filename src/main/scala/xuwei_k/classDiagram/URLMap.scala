package xuwei_k.classDiagram

object URLMap{

  private[this] val SBT_SXR = "http://harrah.github.com/xsbt/latest/sxr/"
  private[this] val GITHUB_SCALA_2_9_1 = "https://github.com/scala/scala/blob/v2.9.1/src/"
  private[this] val LINE1 = ".scala#L1"

  def apply(name: String):String = {
    val fullName = name.split("""\$\$anon""").head
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
      "http://java.sun.com/javase/ja/6/docs/ja/api/" + path + ".html"
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
    } else ""
  }
}

