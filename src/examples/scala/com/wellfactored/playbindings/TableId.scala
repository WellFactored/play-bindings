package com.wellfactored.playbindings

import play.api.mvc.PathBindable
import shapeless.{::, Generic, HNil, Lazy}

case class UserId(id: Long) extends AnyVal

object Test {


  implicit def genericPathBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], bindable: PathBindable[V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        bindable.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, value: String): Either[String, W] =
        bindable.bind(key, value).right.map(v => gen.value.from(v :: HNil))
    }

  def pb(u: UserId)(implicit b: PathBindable[UserId]) = {
    b.bind("userId", "5")
    b.unbind("userId", i)
  }

  val i = UserId(5)

  pb(i)
}

