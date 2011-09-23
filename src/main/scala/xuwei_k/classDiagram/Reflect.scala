package xuwei_k.classDiagram

object Reflect {

  //Interfaceの数によって、グループ分けして返す
  // TODO not used ?
  private def groupByInterfaceCount(classList: List[Class[_]]) = {

    val classNames = classList.map { c => (c, c.getSimpleName) }

    //class名が重複しているclassのリスト
    val overlap =

      classNames.map {
        case (c, name) =>

          (classNames.count { case (_, y) => name == y }, c)

      }.filterNot {
        case (count, name) =>
          count == 1
      }.map { case (_, name) => name }

    println(overlap.map { _.getSimpleName }.distinct); println //debug

    classList.groupBy { c =>
      c.getInterfaces.size
    }.map {
      case (i, list) =>
        (i,
          list.map { e =>
            if (overlap.contains(e)) {
              //クラス名と、そのひとつ外側のパッケージ名
              e.toString.split("""\.""").takeRight(2).reduceLeft(_ + "." + _)
            } else {
              //クラス名のみ
              e.getSimpleName
            }
          })
    }.toList.sortBy { case (i, _) => i }
  }

  private def getSuperClasses(clazz: Class[_]): List[Class[_]] = {

    @scala.annotation.tailrec
    def sub(o: Class[_], result: List[Class[_]]): List[Class[_]] = {
      val superClass = o.getSuperclass
      if (superClass == null) {
        result
      } else {
        sub(superClass, superClass :: result)
      }
    }

    sub(clazz, List(clazz))
  }

  //階層をたどっていってすべての親インターフェイスを探す
  def getAllClassAndTrait(classes: Class[_]*): List[Class[_]] = {

    def sub(c: Class[_], result: List[Class[_]]): List[Class[_]] = {

      val interfaces = c.getInterfaces.toList

      if (interfaces.size == 0) {
        result
      } else {
        interfaces.flatMap { i => sub(i, i :: result) }
      }
    }

    {
      {
        for {
          c <- classes
          clazz <- getSuperClasses(c)
          ret <- sub(clazz, List(clazz))
        } yield {
          ret
        }
      }.toSet -- classes
    }.toList

  }

}
