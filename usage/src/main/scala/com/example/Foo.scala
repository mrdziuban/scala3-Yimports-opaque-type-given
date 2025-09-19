package com.example

import cats.Eq

case class Foo[F[_]](id: F[Int])
object Foo {
  given eq[F[_]](using e: Eq[F[Int]]): Eq[Foo[F]] = Eq.by(_.id)
}
