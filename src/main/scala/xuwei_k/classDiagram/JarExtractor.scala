package xuwei_k.classDiagram

import java.util.jar.JarFile
import scala.jdk.CollectionConverters._
import java.io.File

object JarExtractor{

  def getClassNames(prefix:String,containsAll:Boolean):List[String] = {

    val f = Predef.getClass.getProtectionDomain.getCodeSource.getLocation
    val jarFiles = new File( f.getFile ).getAbsoluteFile.getParentFile.listFiles.filter{_.toString.endsWith(".jar")}
    val clazz = ".class"
    
    jarFiles.toList.flatMap{ j =>
      new JarFile(j).entries.asScala.collect{case s if {
        ( s.isDirectory == false ) && {
          val n = s.toString;

          n.startsWith(prefix) &&
          n.endsWith(clazz) &&
          {if(containsAll) true
           else n.dropRight(clazz.length + 1).contains("$") == false} // drop inner classes
        }}
        => s.toString.dropRight(clazz.length).replace('/','.')
      }.toList
    }
  }
}

