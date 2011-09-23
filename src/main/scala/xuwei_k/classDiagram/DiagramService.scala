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

  /**
   * 実際のインスタンスから作成
   * @param fileName 保存するファイル名
   * @param objList 作成元のオブジェクト
   */
  def createClassDiagramByObj(fileName: String)(objList: AnyRef*): List[Node] = {
    create(fileName, objList.map { _.getClass })
  }

  /**
   * クラスの完全修飾名から作成
   * @param fileName 保存するファイル名
   * @param classNames 作成するクラス名
   */
  def createClassDiagramByName(fileName: String)(classNames: String*): List[Node] = {
    create(fileName, classNames.map { Class.forName(_) })
  }

  def createClassDiagramByClass(fileName: String)(classes: Class[_]*): List[Node] = {
    create(fileName, classes)
  }

  private def create(fileName: String, classes: Traversable[Class[_]]): List[Node] = {

    import collection.{ mutable => mu }

    val result =
      sortByInheritance({
        classes.flatMap { makeClassNodes }.toList ++
          classes.map { c => ClassNode(c, 0, { c.getSuperclass :: c.getInterfaces.toList }: _*) }
      }.distinct.filter { c => !ClassNode.exceptList.contains(c.clazz) })

    //デバック用表示
    result.foreach {
      case (n, list) =>
        println(n, list.size, list.map { _.clazz.getSimpleName })
    }

    //横の位置計算して決めて、ClassNodeオブジェクトのフィールドに保存
    ClassNode.allClassNodes =
      result.flatMap {
        case (_, list) =>
          //        list.zipWithIndex.map{ case (data,n) => data.yoko = n * (((data.level*1.6).asInstanceOf[Int])%2 + 1 ) }
          list.zipWithIndex.map { case (data, n) => data.yoko = rand(n, data.level) }
          list
      }

    //xmlに変換
    ClassNode.allClassNodes.filterNot { x => ClassNode.exceptList.contains(x) }.map { _.toXml }

  }

  /**
   * (ソートしてない)ClassNodeのList作成
   */
  private def makeClassNodes(clazz: Class[_]): List[ClassNode] = {
    getAllClassAndTrait(clazz).map { x =>
      val superClass = x.getSuperclass
      val classes =
        if (superClass != null) {
          superClass :: x.getInterfaces.toList
        } else {
          x.getInterfaces.toList
        }
      ClassNode(x, 0, classes: _*)
    }
  }

  /**
   * ClassNodeのListをソートし、グループ分け
   * (それぞれのlevelの値を変化させる)
   */
  private def sortByInheritance(classList: List[ClassNode]): List[Pair[Int, List[ClassNode]]] = {

    /** ローカルのヘルパー関数,再帰 */
    @scala.annotation.tailrec
    def loop(m: List[ClassNode]) {
      val oldMax = m.foldLeft(0) { (x, y) => x max y.level }
      for (x <- m; y <- m) { //総当りで呼ぶ
        x.compare(y)
      }
      println("loop") //debug
      val newMax = m.foldLeft(0) { (x, y) => x max y.level }
      if (oldMax != newMax) {
        loop(m)
      }
    }

    loop(classList)

    classList.groupBy { _.level }.toList.sortBy(_._1)
  }
}
