package xuwei_k.classDiagram

object URLMap{

  private val SBT_SXR = "http://harrah.github.com/xsbt/latest/sxr/"
  private val EPFL_TRAC_2_9_1 = "http://lampsvn.epfl.ch/trac/scala/browser/scala/tags/R_2_9_1_final/src/" 
  private val TRAC_LINE1 = ".scala#L1"

  def apply(fullName: String):String = {
    val path = fullName.replace(".", "/")

    def s(prefix:String) = fullName.startsWith(prefix)

    if(Seq("ant","cmd","nsc","reflect","util").exists{ p =>s("scala.tools." + p ) }){
      EPFL_TRAC_2_9_1 + "compiler/" + path + TRAC_LINE1
    } else if (s("scala.tools.scalap") ){
      EPFL_TRAC_2_9_1 + "scalap/" + path + TRAC_LINE1 
    } else if (s("scala.")) {
      "http://www.scala-lang.org/api/2.9.1/index.html#" + fullName 
    } else if (s("java")) {
      "http://java.sun.com/javase/ja/6/docs/ja/api/" + path + ".html"
    } else if (s("org.jruby")){
      "http://www.jruby.org/apidocs/" + path + ".html"
    } else if (s("groovy")){
      "http://groovy.codehaus.org/api/" + path + ".html"
    } else if (s("scalaz.")){
      "http://scalaz.github.com/scalaz/scalaz-2.9.1-6.0.2/doc.sxr/" + path + ".scala"
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
      "http://scala-tools.org/mvnsites/liftweb-2.4-M4/#"+ fullName
    } else ""
  }
}

