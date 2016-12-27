[![CircleCI](https://circleci.com/gh/WellFactored/play-bindings.svg?style=svg)](https://circleci.com/gh/WellFactored/play-bindings)
[![Stories in Ready](https://badge.waffle.io/WellFactored/play-bindings.png?label=ready&title=Ready)](https://waffle.io/WellFactored/play-bindings)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bbd834e020d74efabee786d768c2d609)](https://www.codacy.com/app/doug/play-bindings?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=WellFactored/play-bindings&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wellfactored/play-bindings_2.11/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.wellfactored/play-bindings_2.11)

# play-bindings

## Getting started

To use `play-bindings` in your project add the following to your `build.sbt` file:

```
libraryDependencies += "com.wellfactored" %% "play-bindings" % playBindingsVersion
```

The latest version number is shown in the badge above.s

## How and why to use the bindings 
A value class is a case class, of type `W`, that wraps a single value, of type `V` and optionally 
extends `AnyVal`. Value classes are a really convenient and lightweight way of strongly typing 
primitive values to avoid passing them to functions incorrectly. However, when you want to convert
to/from JSON then the default implementations that `Json.format[W]` provides will generate or
 require an extra level of structure that is usually undesirable.

For example, given:

    case class PersonId(id: Long) extends AnyVal
    case class Person(id:PersonId, name:String)

then the default JSON would look like
```
    {
        "id": {
            "id": 1
        },
        "name": "Fred"
    }
```

when we'd rather just have

```
    {
        "id": 1,
        "name": "Fred"
    }
```

To do this you need to manually implement the JSON formatters that wrap and unwrap the structure. You
need to implement similar boilerplate to be able to use the value classes in urls.
This is a pain and adds significant friction to using value classes.

`play-bindings` uses a little bit of Shapeless (https://github.com/milessabin/shapeless) 
to generate all of this boilerplate for you seamlessly. Given `PersonId` as defined above then extending 
the appropriate traits or importing the contents of the relevant objects will let the compiler automatically 
provide instances of the following type classes:

* `Reads[PersonId]` - wraps a `Long` in a `PersonId` when reading from Json
* `Writes[PersonId]` - unwraps the `Long` when writing to Json
* `PathBindable[PersonId]` - decodes a `Long` from a url path segment into a `PersonId`
* `QueryStringBindable` - takes a `Long` value from a query parameter and converts it to a `PersonId`

The value type, `V`, must itself already have a type class instance in scope, e.g. `Reads[String]`. Play provides instances
of all four type classes for all the primitive types.

## Creating the binding instances

There are individual traits that contain implicit functions that will generate the relevant
type classes:

* `trait ValueClassReads`
* `trait ValueClassWrites`
* `trait ValueClassPathBindable`
* `trait ValueClassQueryStringBindable`

There are also some traits and objects that combine the base traits in useful ways:

* `trait ValueClassFormats extends ValueClassReads with ValueClassWrites`
* `object ValueClassFormats extends ValueClassFormats`
* `trait ValueClassUrlBinders extends ValueClassPathBindable with ValueClassQueryStringBindable`
* `object ValueClassUrlBinders extends ValueClassUrlBinders`

If you add the following to the `build.sbt` for your play project:

    routesImport += "com.wellfactored.playbindings.ValueClassUrlBinders._"

then you can bind path elements and query parameters to value classes in your controller methods
without writing any further boilerplate code.

In the code where you are reading and writing JSON values you can either extend the relevant traits
or use `import com.wellfactored.playbindings.ValueClassFormats._` as you prefer.
