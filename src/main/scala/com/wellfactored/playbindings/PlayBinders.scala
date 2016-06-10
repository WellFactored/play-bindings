package com.wellfactored.playbindings

import play.api.libs.json.{JsResult, JsValue, Reads, Writes}
import play.api.mvc.{PathBindable, QueryStringBindable}
import shapeless.{::, Generic, HNil, Lazy}

object PlayBinders {
  implicit def genericPathBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], bindable: PathBindable[V]): PathBindable[W] =
    new PathBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        bindable.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, value: String): Either[String, W] =
        bindable.bind(key, value).right.map(v => gen.value.from(v :: HNil))
    }

  implicit def genericQueryStringBindable[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], binder: QueryStringBindable[V]): QueryStringBindable[W] =
    new QueryStringBindable[W] {
      override def unbind(key: String, wrapper: W): String =
        binder.unbind(key, gen.value.to(wrapper).head)

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, W]] =
        binder.bind(key, params).map(_.right.map(v => gen.value.from(v :: HNil)))
    }

  implicit def genericReads[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], rv: Reads[V]): Reads[W] =
    new Reads[W] {
      override def reads(json: JsValue): JsResult[W] = rv.reads(json).map(v => gen.value.from(v :: HNil))
    }

  implicit def genericWrites[W, V](implicit gen: Lazy[Generic.Aux[W, V :: HNil]], wv: Writes[V]): Writes[W] =
    new Writes[W] {
      override def writes(w: W): JsValue = wv.writes(gen.value.to(w).head)
    }
}
