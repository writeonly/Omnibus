package pl.writeonly.omnibus.rule

import io.vavr.collection.Stream
import io.vavr.control.Option

typealias LazyApply<A> = () -> A
typealias LazyOptionApply<A> = () -> Option<A>

object LazyUtil {
    fun <A> applyWithDefault(d: LazyApply<A>, stream: Stream<LazyOptionApply<A>>): A = apply(stream).getOrElse(d)

    fun <A> apply(stream: Stream<LazyOptionApply<A>>): Option<A> = stream
        .map { it() }
        .flatMap(Option<A>::toStream)
        .headOption()
}
