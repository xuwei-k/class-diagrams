package xuwei_k.classDiagram

import scala.xml.Node

sealed abstract class XmlResource{

  val nodes : List[Node]
  val title : String
  val contentType:String
  val mkString:String
  val toXML : Node
  val width : Int = 5000
  val height: Int = 2000

  implicit def int2String(i:Int) = i.toString

  protected val makeSVGnode: Node = {
    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" id="body" width={ width } height={ height }
     viewBox={ Seq(-10,-10, width,height).mkString(" ") } >
     <title>{title}</title>
     { nodes }
    </svg>
  }
}

case class SVG(
    nodes : List[Node],
    title : String
  ) extends XmlResource{

  val contentType = "image/svg+xml"

  val doctype = """<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.0//EN" "http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd">"""

  val toXML = makeSVGnode

  val mkString:String = {
    doctype + makeSVGnode
  }
}

case class HTML(
    nodes : List[Node],
    title : String
  ) extends XmlResource{

  val contentType = "text/html"
  val doctype = "<!DOCTYPE html>" 
  val toXML = {
    <html lang="en">
      {header(title)}
    <body>{ tweetButton("#scala") }{ googlePlusOne }<a href={title + ".svg"} >pure svg version</a>{ makeSVGnode }</body>
    </html>
  }

  val mkString = { doctype + toXML.toString }

  def header(title:String):Node = {
    <head>
      <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
      <title>{ title }</title>
      <script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
    </head>
  }

  def tweetButton(data: String):Node = {
    <p>
      <a href="http://twitter.com/share" class="twitter-share-button" data-text={ data } data-count="horizontal">Tweet</a>
      <script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
    </p>
  }

  val googlePlusOne:Node = <g:plusone></g:plusone>
}

