package com.example

case class Foo[F[_]](id: F[Int])
object Foo {
  given numeric[F[_]](using o: Numeric[F[Int]]): Numeric[Foo[F]] = ???
}

val fooNumeric = Numeric[Foo[OpaqueType]]
