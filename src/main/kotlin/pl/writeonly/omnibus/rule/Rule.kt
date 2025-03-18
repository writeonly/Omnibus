package pl.writeonly.omnibus.rule

import io.vavr.PartialFunction

import io.vavr.collection.List
import io.vavr.control.Option

interface Rule<A, B>: PartialFunction<A, B> {

    companion object {
        fun <A, B> find(rules: List<PartialFunction<A, B>>, context: A): Option<B> {
            return rules.find { it.isDefinedAt(context) }.map { it.apply(context) }
        }
        fun <A, B> filter(rules: List<PartialFunction<A, B>>, context: A): List<B> {
            return rules.filter { it.isDefinedAt(context) }.map { it.apply(context) }
        }
    }
}
