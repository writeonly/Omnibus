package pl.writeonly.omnibus.jakarta.common.rule

import io.vavr.PartialFunction
import io.vavr.collection.List
import io.vavr.control.Option

interface Rule<A, B> : PartialFunction<A, B> {

    companion object {
        fun <A, B> find(rules: List<PartialFunction<A, B>>, context: A): Option<B> =
            rules.find { it.isDefinedAt(context) }.map { it.apply(context) }

        fun <A, B> filter(rules: List<PartialFunction<A, B>>, context: A): List<B> =
            rules.filter { it.isDefinedAt(context) }.map { it.apply(context) }
    }
}
