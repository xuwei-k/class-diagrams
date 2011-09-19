package xuwei_k.classDiagram
import javax.servlet.http._
import scala.util.control.Exception._

class Front extends HttpServlet {
  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {

//    Seq[HttpServletRequest => Any](_.getServletPath, _.getRequestURI, _.getRequestURL).foreach(f => println(f(request)))

    val name = catching(classOf[Exception]) opt request.getRequestURI.tail.replace(".svg", "")

    response.getWriter.println {
      name.map { className =>

        println(className)

        createSVG(className) match {
          case None => <h1>{ "%s not exists".format(className) }</h1>
          case Some(SVG(_, xml)) => {
//            response.setContentType("image/svg+xml")
              response.setContentType("text/html")
            xml
          }
        }
      }.getOrElse("error")
    }
  }
  
  def createSVG[A](className: String): Option[SVG] = {
    {
      catching(classOf[ClassNotFoundException]) opt
        (Class.forName(className).asInstanceOf[Class[A]])
    }.map { clazz =>
      DiagramService.createClassDiagramByClass(className)(clazz)
    }
  }

}