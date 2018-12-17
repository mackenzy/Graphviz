package ua.mackenzy.graphviz


import scala.util.{Properties, Try}

object MainObject {
  def main(args: Array[String]): Unit = {
    val text = Try(args(0)).getOrElse("")
    val graphDef = GraphvizBuilder.buildDefFrom(text)
    println("Output:")
    println(graphDef)
  }
}

case class Node(name: String, children: Seq[Node])

object GraphvizBuilder {

  def buildDefFrom(raw: String): String = {
    val nodes = parse(raw)
    val gLinks = combine(None, nodes)
    graphDef(gLinks)
  }

  def combine(root: Option[String], nodes: Seq[Node]): Seq[String] = {
    val gRootLinks = root
      .map(r => nodes.map(n => s"$r -> ${n.name}"))
      .getOrElse(Seq.empty)
    val gChildLinks = nodes
      .flatMap(n => combine(Some(n.name), n.children))
    gRootLinks ++ gChildLinks
  }

  def graphDef(gLinks: Seq[String]): String = {
    val ls = Properties.lineSeparator
    gLinks.mkString(s"digraph G {$ls", ls, s"$ls}")
  }

  def parse(raw: String, level: Int = 0, idx: Int = 0): Seq[Node] = {
    val lp = raw.indexOf('(')
    val lb = raw.indexOf('[')

    if (lp == -1 || (-1 < lb && lb < lp)) {
      val name = s"${raw.simplified}$level$idx"
      Seq(Node(name, Seq.empty))
    } else {
      val smplf = raw.substring(0, lp).simplified
      val name = s"$smplf$level$idx"
      val right = raw.substring(lp + 1, raw.length).splitBy(',')
      val nodes = right.zipWithIndex.flatMap { case (r, i) => parse(r, level + 1, i) }
      Seq(Node(name, nodes))
    }
  }

  implicit class StringUtils(str: String) {

    def splitBy(s: Char): Array[String] = {
      var cs = Seq.empty[Int]
      var lbCount = 0
      var lpCount = 0
      for (i <- 0 until str.length) {
        val c = str(i)
        c match {
          case '[' => lbCount += 1
          case ']' => lbCount -= 1
          case '(' => lpCount += 1
          case ')' => lpCount -= 1
          case _ =>
        }
        if (lbCount <= 0 && lpCount <= 0 && c == s) cs = cs :+ i
      }
      if (cs.isEmpty) Array(str)
      else {
        cs = -1 +: cs :+ str.length
        cs
          .sliding(2)
          .map(l => str.substring(l.head + 1, l.last))
          .toArray
      }
    }

    def simplified: String = {
      val lb = str.indexOf('[')
      if (lb == -1)
        str.filter(c => c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')
      else {
        val name = str.substring(0, lb)
        val suffix = str
          .substring(lb + 1)
          .split('(')
          .take(2)
          .map(_.charAt(0))
          .mkString
        s"$name$suffix"
      }
    }
  }

}
