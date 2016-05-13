[![Stories in Ready](https://badge.waffle.io/WellFactored/play-bindings.png?label=ready&title=Ready)](https://waffle.io/WellFactored/play-bindings)
[![Build Status](https://travis-ci.org/WellFactored/play-bindings.svg?branch=master)](https://travis-ci.org/WellFactored/play-bindings)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bbd834e020d74efabee786d768c2d609)](https://www.codacy.com/app/doug/play-bindings?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=WellFactored/play-bindings&amp;utm_campaign=Badge_Grade)

# play-bindings
 
A value class is a case class, of type `W`, that wraps a single value, of type `V` and optionally 
extends `AnyVal`. Value classes are a really convenient and lightweight way of strongly typing 
primitive values to avoid passing them to functions incorrectly, but the overhead of creating the
json formatters and parameter binding typeclass instances can be a drag. This library provides a 
convenient way to eliminate the boilerplate associated with those typeclasses. 

An example, given:

    case class PersonId(id: Long) extends AnyVal

play-bindings will create implicit typeclass instances for the following:

* `Reads[PersonId]` - wraps a `Long` in a `PersonId` when reading from Json
* `Writes[PersonId]` - unwraps the `Long` when writing to Json
* `PathBindable[PersonId]` - decodes a `Long` from a url path segment into a `PersonId`
* `QueryStringBindable` - takes a `Long` value from a query parameter and converts it to a `PersonId`

The value type, `V`, must itself already have typeclass instance in scope. Play provides instances
of all four type classes for all the primitive types.

## Creating the binding instances

The library provides two mechanisms for creating the bindings. You can instantiate a new instance of
the `PlayBindings` class and import its members into scope, like this:

    import com.wellfactored.playbindings.PlayBindings
    val pvw = PlayBindings[Long, PersonId]()
    import pvw._

Alternatively, you can mix a trait into the companion object of the value class, like this:
 
    import com.wellfactored.playbindings.SimpleWrapper
    object PersonId extends SimpleWrapper[Long, PersonId]
    
The compiler will look in the companion object to find the typeclass instances if it can't
find them in the implicit scope.

Sometimes you may want to apply validation to the value before wrapping it in the value class instance.
In this case you can use a different trait, `ValidatingWrapper` like this:

    import com.wellfactored.playbindings.ValidatingWrapper
    object UserId extends ValidatingWrapper[Long, UserId] {
      override def validate(v: Long): Either[String, Long] = if (v > 0) Right(v) else Left("id must be greater than zero")
    }

If validation fails then Play will nicely deal with passing the error message back out as part
of its normal error handling mechanisms.
    
## Dependencies

`play-bindings` is built against Play 2.4.3. It should be forward compatible with Play 2.5
