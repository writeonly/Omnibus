package pl.writeonly.omnibus.rule

import io.vavr.Function1
import io.vavr.collection.List
import io.vavr.control.Option

interface LiftedRule<A, B> : Function1<A, Option<B>> {
    companion object {
        fun <A, B> find(rules: List<Function1<A, Option<B>>>, context: A): Option<B> =
            rules.map { it.apply(context) }.find { it.isDefined }.flatMap { it }

        fun <A, B> filter(rules: List<Function1<A, Option<B>>>, context: A): List<B> =
            rules.map { it.apply(context) }.filter { it.isDefined }.flatMap { it }
    }
}
