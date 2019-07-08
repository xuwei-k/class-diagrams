package xuwei_k.classDiagram

import Reflect.getAllClassAndTrait
import scala.xml.Node

object DiagramService {

  /**
   * x > 0　かつ y > 0の引数が渡ってきたときに、
   * xについて単射になるように、かつ大きくなりすぎないように返す
   */
  private def rand(x: Int, y: Int): Int = {
    (x * (y % 2 + 1)) + y % 3
  }

  /** 実際のインスタンスから作成
   * @param name 
   * @param objList 作成元のオブジェクト
   */
  def createClassDiagramByObj(name: String)(objList: AnyRef*): List[Node] = {
    create(name, objList.map { _.getClass })
  }

  /** クラスの完全修飾名から作成
   * @param name
   * @param classNames 作成するクラス名
   */
  def createClassDiagramByName(name: String)(classNames: String*): List[Node] = {
    create(name, classNames.map { Class.forName(_) })
  }

  def createClassDiagramByClass(name: String)(classes: Class[_]*): List[Node] = {
    create(name, classes)
  }

  private def create(name: String, classes: Iterable[Class[_]]): List[Node] = {

    val result =
      sortByInheritance({
        classes.flatMap { makeClassNodes }.toList ++
          classes.map { c => ClassNode(c, 0, 0, c.getSuperclass :: c.getInterfaces.toList) }
      }.distinct.filter { c => !ClassNode.exceptList.contains(c.clazz) })

    //横の位置計算して決めて、ClassNodeオブジェクトのフィールドに保存
    val allClassNodes =
      result.flatMap {
        case (_, list) =>
          //        list.zipWithIndex.map{ case (data,n) => data.yoko = n * (((data.level*1.6).asInstanceOf[Int])%2 + 1 ) }
          list.zipWithIndex.map { case (data, n) => data.copy(yoko = rand(n, data.level)) }
      }

    //xmlに変換
    allClassNodes.filterNot { x => ClassNode.exceptList.contains(x) }.map { _.toXml(allClassNodes) }
  }

  /** (ソートしてない)ClassNodeのList作成
   */
  private def makeClassNodes(clazz: Class[_]): List[ClassNode] = {
    getAllClassAndTrait(clazz).map { x =>
      val interfaces = x.getInterfaces.toList
      val classes = Option(x.getSuperclass).map{_ :: interfaces }.getOrElse(interfaces)
      ClassNode(x, 0, 0, classes)
    }
  }

  /**
   * ClassNodeのListをソートし、グループ分け
   * (それぞれのlevelの値を変化させる)
   */
  private def sortByInheritance(classList: List[ClassNode]): List[(Int, List[ClassNode])] = {
    def levelMap(nodes: List[ClassNode]): Map[Int, Int] =
      nodes.distinct.map{_.level}.groupBy(identity).view.mapValues(_.size).toMap

    @annotation.tailrec
    def loop(m: List[ClassNode]): List[ClassNode] = {
      val newNodes = {
        for (x <- m; y <- m) yield x.cmp(y)
      }.flatten.groupBy(_.clazz).values.map(_.maxBy(_.level)).toList

      if(levelMap(m) == levelMap(newNodes)){
        m
      }else{
        loop(newNodes)
      }
    }

    loop(classList).groupBy { _.level }.toList.sortBy(_._1)
  }
}
