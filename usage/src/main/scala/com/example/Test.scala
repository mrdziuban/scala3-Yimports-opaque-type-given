package com.example

import cats.Eq
// import com.example.types.OpaqueType

object test {
  val fooEq = Eq[Foo[OpaqueType]]
}
