package xuwei_k.classDiagram
import javax.servlet.http._
import scala.util.control.Exception.catching
import scala.xml.Node

class Front extends HttpServlet {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {

    val uri  = req.getRequestURI
    val name = catching(classOf[Exception]).opt{ uri.tail.replace(".svg", "") }

    resp.getWriter.println {
      name.map { className =>

        createNodeList(className) match {
          case None =>
             <h1>{ "%s not exists".format(className) }</h1>
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
  }
  
  def createNodeList[A](className: String): Option[List[Node]] = {
    catching(classOf[ClassNotFoundException]).opt{
      (Class.forName(className).asInstanceOf[Class[A]])
    }.map{ clazz =>
      DiagramService.createClassDiagramByClass(className)(clazz)
    }
  }

}
