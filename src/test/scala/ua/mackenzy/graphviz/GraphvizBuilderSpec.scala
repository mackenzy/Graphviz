package ua.mackenzy.graphviz

import org.scalatest.{FlatSpec, Matchers}

class GraphvizBuilderSpec extends FlatSpec with Matchers {

  "GraphvizBuilder" should "build definition from provided example" in {
    val text = "PackageDef(Ident(<empty>),List(ValDef(Trace,TypeTree[TypeRef(ThisType(TypeRef(NoPrefix,module class <empty>)),module class Trace$)])))"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> Ident10
        |PackageDef00 -> List11
        |Ident10 -> empty20
        |List11 -> ValDef20
        |ValDef20 -> Trace30
        |ValDef20 -> TypeTreeTT31
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from simple string" in {
    val text = "PackageDef(<empty>)"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> empty10
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from simple string level 3" in {
    val text = "PackageDef(Ident(<empty>))"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> Ident10
        |Ident10 -> empty20
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from simple string level 3 an comma" in {
    val text = "PackageDef(Ident(<empty>), Object)"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> Ident10
        |PackageDef00 -> Object11
        |Ident10 -> empty20
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from string level 3 an 2 commas" in {
    val text = "PackageDef(Ident(<empty>), Object, Some(NewObject))"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> Ident10
        |PackageDef00 -> Object11
        |PackageDef00 -> Some12
        |Ident10 -> empty20
        |Some12 -> NewObject20
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from string level 7 and list" in {
    val text = "PackageDef(Ident(<empty>), List(List(ValDef(Trace), List(ValDef(Ident(<empty>), NoPrefix)))))"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> Ident10
        |PackageDef00 -> List11
        |Ident10 -> empty20
        |List11 -> List20
        |List20 -> ValDef30
        |List20 -> List31
        |ValDef30 -> Trace40
        |List31 -> ValDef40
        |ValDef40 -> Ident50
        |ValDef40 -> NoPrefix51
        |Ident50 -> empty60
        |}
      """.trim.stripMargin
    )
  }

  it should "build definition from string level 7 and tough data types" in {
    val text = """PackageDef(List(List(TypeTree[TypeRef(ThisType(TypeRef(NoPrefix,module class <empty>)),module class Trace$)],List(ValDef(Nested(TypeTree[TypeRef(WithPrefix,module class HelloWorld)]),NoPrefix)))),Ident(<empty>))"""
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |PackageDef00 -> List10
        |PackageDef00 -> Ident11
        |List10 -> List20
        |List20 -> TypeTreeTT30
        |List20 -> List31
        |List31 -> ValDef40
        |ValDef40 -> Nested50
        |ValDef40 -> NoPrefix51
        |Nested50 -> TypeTreeTW60
        |Ident11 -> empty20
        |}
      """.trim.stripMargin
    )
  }

  it should "return an empty graph on empty input" in {
    val text = ""
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |
        |}
      """.trim.stripMargin
    )
  }

  it should "return an empty graph on word without parenthesis input" in {
    val text = "BlaBlaBla"
    val definition = GraphvizBuilder.buildDefFrom(text)

    definition should equal(
      """
        |digraph G {
        |
        |}
      """.trim.stripMargin
    )
  }


}
