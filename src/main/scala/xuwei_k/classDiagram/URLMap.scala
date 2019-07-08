package xuwei_k.classDiagram

object URLMap{

  @inline final val GITHUB = "https://github.com/"
  @inline final val GITHUB_SCALA = GITHUB + "scala/scala/blob/v2.13.0/src/"
  @inline final val LINE1 = ".scala#L1"
  private[this] val SCALAZ_GITHUB = "http://github.com/scalaz/scalaz/blob/v7.2.28/"

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
      "https://docs.oracle.com/javase/8/docs/api/" + path + ".html"
    } else if (s("scalaz")){
      val module = fullName.split('.')(1) match {
        case "concurrent" => "concurrent"
        case "example"    => "example"
        case "iteratee"   => "iteratee"
        case "scalacheck" => "scalacheck-binding"
        case _ =>
          if(fullName.contains("effect")) "effect"
          else "core"
      }
      SCALAZ_GITHUB + module + "/src/main/scala/" + path + ".scala"
    } else if (s("specs2.") || s("org.specs2.")){
      "https://etorreborre.github.io/specs2/api/SPECS2-4.6.0/index.html#" + fullName
    } else ""
  }
}

