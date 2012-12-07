import sbt._

object PluginDef extends Build {
  lazy val root = Project("plugins", file(".")) dependsOn(
    uri("git://github.com/sbt/sbt-appengine#d2778bd4c3acc8495555a964a92404fb67264fe1")
   ,uri("git://github.com/rtimush/sbt-updates#04aa05573214fd3c5c57d")
  )
}

