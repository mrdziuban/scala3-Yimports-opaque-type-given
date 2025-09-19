package com.example.types

opaque type OpaqueType[A] = Int
object OpaqueType {
  given numeric[A]: Numeric[OpaqueType[A]] = Numeric[Int]
}
