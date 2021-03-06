package xuwei_k.classDiagram

import Reflect.getAllClassAndTrait
import scala.xml._

/**
 * @param clazz 自身のclass
 * @param level 自身のsubclassが多いほど大きくなる
 * @param parents 直接の親のリスト
 */
case class ClassNode(clazz: Class[_], level: Int, yoko: Int, parents: List[Class[_]]) {
  import ClassNode._

  /** 間接的なものも含めた、すべての親 */
  lazy val allParents = getAllClassAndTrait(this.clazz)

  def cmp(that: ClassNode): List[ClassNode] = {
    def noChange = this :: that :: Nil
    if (this.parents.contains(that.clazz)) {
      if (that.parents.contains(this.clazz)) {
        throw new Error("循環参照？ " + this.clazz + " " + that.clazz)
      } else {
        if (that.level <= this.level) {
          this :: that.copy(level = this.level + 1) :: Nil
        } else {
          noChange
        }
      }
    } else {
      if (that.parents.contains(this.clazz)) {
        if (that.level >= this.level) {
          this.copy(level = that.level + 1) :: that :: Nil
        }else{
          noChange
        }
      } else {
        if (this.allParents.contains(that.clazz)) {
          if (that.level <= this.level) {
            this :: that.copy(level = this.level + 1) :: Nil
          }else{
            noChange
          }
        } else if (that.allParents.contains(this.clazz)) {
          if (that.level >= this.level) {
            this.copy(level = that.level + 1) :: that :: Nil
          } else {
            noChange
          }
        } else {
          noChange
        }
      }
    }
  }

  private lazy val fullName = clazz.getName
  private val url = URLMap(fullName)

  def toXml(allClassNodes: List[ClassNode]): Node = {
    val packageFontSize = 10
    val maxFontSize = 18
    val baseX = yoko * w
    val baseY = level * h
    val middleX = baseX + (recW / 2)
    val simpleName = clazz.getSimpleName
    val tmpSize = (recW * 2) / simpleName.length
    val fontSize = if (tmpSize < maxFontSize) { tmpSize } else { maxFontSize }

    <g>
      <a xlink:href={ url } target="_blank">
        <rect x={ baseX } y={ baseY } width={ recW } height={ recH } fill={ if (clazz.isInterface) "#799F5A" else "#7996AC" } stroke="black" stroke-width="2">
        </rect>
        <text x={ baseX + 5 } y={ baseY + 15 } font-size={ packageFontSize }>{ fullName.replace(simpleName, "") }</text>
        <text x={ baseX + 5 } y={ baseY + packageFontSize + 24 } font-size={ fontSize }>{ simpleName }</text>
      </a>
      {
        for {
          p <- parents
          if !exceptList.contains(p)
          s <- allClassNodes.find { _.clazz == p }
        } yield {
          <line x1={ middleX } y1={ baseY + recH } x2={ s.yoko * w + (recW / 2) } y2={ s.level * h } stroke="black" stroke-width="1"/>
        }
      }
    </g>
  }
}

object ClassNode {
  private val w = 120 //基準位置の横幅
  private val h = 200 //基準位置の縦幅
  private val recW = w - 20 //四角形の幅
  private val recH = h - 160 //四角形の高さ

  //線をひくのをやめるやつ
  lazy val exceptList = List("java.lang.Object").map { Class.forName }

  @inline private implicit def int2String(e: Any): String = e.toString
}
