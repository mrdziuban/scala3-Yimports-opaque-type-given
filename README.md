# Scala3 issue with `-Yimport`s and `given`s for an exported `opaque type`

This demonstrates an issue with `-Yimports` and `given` instances for an exported `opaque type` -- when the type is imported from an object included in `-Yimports`, a `given` in its companion object may not be discovered.

There are a few files involved here:

1. [`OpaqueType.scala`](predef/src/main/scala/com/example/types/OpaqueType.scala) -- the definition of the `opaque type`
    - The `OpaqueType` companion object defines a `Numeric` instance
2. [`Predef.scala`](predef/src/main/scala/com/example/Predef.scala) -- the `Predef` object that's added to `-Yimports`
    - `OpaqueType` is `export`ed from `object Predef`
3. [`Foo.scala`](usage/src/main/scala/com/example/Foo.scala) -- the definition of `case class Foo`
    - The `Foo` companion object defines a `Numeric` instance that requires an `Numeric[F[Int]]`
    - `val fooNumeric` tries to summon an instance of `Numeric[Foo[OpaqueType]]`, but the implicit search fails with an error:
        ```scala
        -- [E172] Type Error: /Users/matt/scala3-yimports-given/usage/src/main/scala/com/example/Foo.scala:8:41
        8 |val fooNumeric = Numeric[Foo[OpaqueType]]
          |                                         ^
          |No given instance of type Numeric[com.example.Foo[OpaqueType]] was found for parameter num of method apply in object Numeric.
          |I found:
          |
          |    com.example.Foo.numeric[[A] =>> com.example.types.OpaqueType[A]](
          |      com.example.types.OpaqueType.numeric²[A²])
          |
          |But given instance numeric² in object OpaqueType does not match type Numeric[([A] =>> com.example.types.OpaqueType[A])[Int]]
          |
          |where:    A        is a type variable
          |          A²       is a type variable
          |          numeric  is a given instance in object Foo
          |          numeric² is a given instance in object OpaqueType
        ```

## Notes

All of the following changes make things work as expected:

### 1. Explicitly importing `OpaqueType`

It works if `import com.example.types.OpaqueType` is added to `Foo.scala` instead of relying on the predef import.

### 2. Using a type alias in `Predef` instead of `export`ing

It works if `Predef` contains a type alias instead of an `export`:

```scala
type OpaqueType[A] = com.example.types.OpaqueType[A]
```

### 3. Using `Ordering` instead of `Numeric`

I don't understand why, but implicit resolution works fine when searching for an `Ordering` instance instead of `Numeric`.

See this branch: https://github.com/mrdziuban/scala3-Yimports-opaque-type-given/compare/ordering
