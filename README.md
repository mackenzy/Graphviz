###GraphvizBuilder

Generate the Graphviz definition for the below Tree (for ease of testing: http://www.webgraphviz.com/) 

```
"PackageDef(Ident(<empty>),List(ValDef(Trace,TypeTree[TypeRef(ThisType(TypeRef(NoPrefix,module class <empty>)),module class Trace$)])))"
```

Must produce:

```
digraph G {
  PackageDef00 -> Ident10
  PackageDef00 -> List11
  Ident10 -> empty20
  List11 -> ValDef20
  ValDef20 -> Trace30
  ValDef20 -> TypeTreeTT31
}
```

###Run:

```
sbt "runMain ua.mackenzy.graphviz.MainObject \"PackageDef(Ident(<empty>),List(ValDef(Trace,TypeTree[TypeRef(ThisType(TypeRef(NoPrefix,module class <empty>)),module class Trace$)])))\""
```