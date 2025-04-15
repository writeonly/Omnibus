package pl.writeonly.omnibus.jakarta.common.rule

import io.vavr.collection.Stream
import io.vavr.control.Option

typealias LazyApply<A> = () -> A
typealias LazyOptionApply<A> = () -> Option<A>

object LazyUtil {
    fun <A> Stream<LazyOptionApply<A>>.apply(): Option<A> =
        map { it() }
            .flatMap(Option<A>::toStream)
            .headOption()

    fun <A> Stream<LazyOptionApply<A>>.applyWithDefault(default: LazyApply<A>): A =
        apply().getOrElse(default)
}
