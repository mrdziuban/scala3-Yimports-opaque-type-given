package com.example.types

import cats.Eq

opaque type OpaqueType[+A] = Unit
object OpaqueType {
  given eq[A]: Eq[OpaqueType[A]] = Eq.allEqual
}
