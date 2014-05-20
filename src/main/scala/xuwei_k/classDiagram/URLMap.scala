package xuwei_k.classDiagram

object URLMap{

  @inline final val GITHUB = "https://github.com/"
  @inline final val GITHUB_SCALA = GITHUB + "scala/scala/blob/v2.11.1/src/"
  @inline final val LINE1 = ".scala#L1"
  private[this] val SCALAZ_GITHUB = "http://github.com/scalaz/scalaz/blob/v7.1.0-M7/"

  def apply(name: String):String = {
    val fullName = name.split("""\$""").head
    val path = fullName.replace(".", "/")

    def s(prefix:String) = fullName.startsWith(prefix)

    if(Seq("ant","cmd","nsc","reflect","util").exists{ p =>s("scala.tools." + p ) }){
      GITHUB_SCALA + "compiler/" + path + LINE1
    } else if (s("scala.tools.scalap") ){
      GITHUB_SCALA + "scalap/" + path + LINE1
    } else if (s("scala.")) {
      GITHUB_SCALA + "library/" + path + LINE1
    } else if (s("java")) {
      "http://docs.oracle.com/javase/7/docs/api/" + path + ".html"
    } else if (s("scalaz")){
      val module = fullName.split('.')(1) match {
        case "concurrent" => "concurrent"
        case "example"    => "example"
        case "iteratee"   => "iteratee"
        case "scalacheck" => "scalacheck-binding"
        case "xml"        => "xml"
        case "typelevel"  => "typelevel"
        case _ =>
          if(fullName.contains("effect")) "effect"
          else "core"
      }
      SCALAZ_GITHUB + module + "/src/main/scala/" + path + ".scala"
    } else if (s("specs2.") || s("org.specs2.")){
      "http://etorreborre.github.io/specs2/api/SPECS2-2.3.12/index.html#" + fullName
    } else ""
  }
}

