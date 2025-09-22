package com.example.types

opaque type OpaqueType[A] = Int
object OpaqueType {
  given ordering[A]: Ordering[OpaqueType[A]] = Ordering[Int]
}
