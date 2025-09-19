# Scala3 issue with `-Yimport`s and `given`s for an exported `opaque type`

This demonstrates an issue with `-Yimports` and `given` instances for an exported `opaque type` -- when the type is imported from an object included in `-Yimports`, a `given` in its companion object may not be discovered.

There are a few files involved here:

1. [`OpaqueType.scala`](predef/src/main/scala/com/example/types/OpaqueType.scala) -- the definition of the `opaque type`
    - The `OpaqueType` companion object defines a `cats.Eq` instance
2. [`Predef.scala`](predef/src/main/scala/com/example/Predef.scala) -- the `Predef` object that's added to `-Yimports`
    - `OpaqueType` is `export`ed from `object Predef`
3. [`Foo.scala`](usage/src/main/scala/com/example/Foo.scala) -- the definition of `case class Foo`
    - The `Foo` companion object defines a `cats.Eq` instance that requires an `Eq[F[Int]]`
4. [`Test.scala`](usage/src/main/scala/com/example/Test.scala) -- the test that tries to summon an `Eq[Foo[OpaqueType]]`
    - The implicit search fails with an error:
        ```scala
        -- [E172] Type Error: /Users/matt/scala3-Yimports-opaque-type-given/usage/src/main/scala/com/example/Test.scala:7:33
        7 |  val fooEq = Eq[Foo[OpaqueType]]
          |                                 ^
          |No given instance of type cats.kernel.Eq[com.example.Foo[OpaqueType]] was found for parameter ev of method apply in object Eq.
          |I found:
          |
          |    com.example.Foo.eq[[A] =>> com.example.types.OpaqueType[A]](
          |      com.example.types.OpaqueType.ordering[A²])
          |
          |But given instance ordering in object OpaqueType does not match type cats.kernel.Eq[([A] =>> com.example.types.OpaqueType[A])[Int]]
          |
          |where:    A  is a type variable
          |          A² is a type variable
        ```

## Notes

Either of the following changes make things work as expected:

### Explicitly importing `OpaqueType`

It works if `import com.example.types.OpaqueType` is added to `Test.scala` instead of relying on the predef import.

### Using a type alias in `Predef` instead of `export`ing

It works if `Predef` contains a type alias instead of an `export`:

```scala
type Never[+A] = com.example.types.Never[A]
```
