package com.example

case class Foo[F[_]](id: F[Int])
object Foo {
  given ordering[F[_]](using o: Ordering[F[Int]]): Ordering[Foo[F]] = ???
}

val fooOrdering = Ordering[Foo[OpaqueType]]
