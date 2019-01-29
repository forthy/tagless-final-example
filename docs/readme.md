# Tagless Final Example

To install my project
```scala
libraryDependencies += "com.example" % "tagless-final-sample" % "@VERSION@"
```

```scala mdoc
val x = 1
List(x, x)

import com.example.uftest1206182.models.newtype.SampleNewTypes._

val atest: ATest = ATest("Hello")

atest.msg
```