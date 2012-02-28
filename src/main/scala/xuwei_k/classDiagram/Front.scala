package xuwei_k.classDiagram
import javax.servlet.http._
import scala.util.control.Exception.catching
import scala.xml.{Elem,Node}

class Front extends HttpServlet {
  val ALL_CLASS = "allclass"

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {

    val uri  = req.getRequestURI
    val name = catching(classOf[Exception]).opt{ uri.tail.replace(".svg", "") }
    val allClass = req.getParameter(ALL_CLASS) != null

    val result =
    if(name == Some("") || name == None){
      template(printClassList("",allClass),"class diagrams")
    }else{
      name.map { className =>
        createNodeList(className) match {
          case None =>
            template(printClassList(className,allClass),className)
          case Some(nodes) => {
            val resource =
              if(uri.endsWith(".svg")){
                SVG( nodes,className)
              }else{
                HTML(nodes,className)
              }

            resp.setContentType(resource.contentType)
            resource.mkString
          }
        }
      }.getOrElse(<h1>error</h1>)
    }

    resp.getWriter.println(result)

  }

  def createNodeList[A](className: String): Option[List[Node]] = {
    catching(classOf[ClassNotFoundException]).opt{
      (Class.forName(className).asInstanceOf[Class[A]])
    }.map{ clazz =>
      DiagramService.createClassDiagramByClass(className)(clazz)
    }
  }


  def printClassList(prefix:String,containsAll:Boolean):Elem = {
    val list = JarExtractor.getClassNames(prefix.replace('.','/'),containsAll)

    <div>
    <p><span style="font-size:x-large;">{list.size}</span> classes</p>
    <p>{
      if(containsAll){
        <a href={"/"+prefix }>change only outer classes</a>
      }else{
        <a href={"/"+prefix+"?"+ALL_CLASS}>change all classes(contain inner classes)</a>
      }
    }</p>
    <ul>{list.map{ name =>
      <li><a href={ name } >{name}</a><a class={SVG_LINK} href={ name + ".svg"}>SVG</a></li>
    }}</ul>
    </div>
  }

  val SVG_LINK = "svglink"

  val doctype = "<!DOCTYPE html>"

  val STYLE = """ a.""" + SVG_LINK + """{ margin-left:30px; } """

  def template(body:Elem,title:String):Elem = {
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <title>{title}</title>
    <link rel="shortcut icon" href="./favicon.ico" />
    <script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
    <style type="text/css">
      { STYLE }
    </style>
  </head>
  <body>
    <p>{ HTML.tweetButton("#scala") }{ HTML.googlePlusOne }<a href="https://github.com/xuwei-k/class-diagrams">source code</a>
      <img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Powered by Google App Engine" />
      Developing by <a href="http://twitter.com/#!/xuwei_k" target="_blank">xuwei_k</a>
    </p>
    {body}
  </body>
</html>
  }
}
