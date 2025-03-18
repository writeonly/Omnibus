package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.Function1
import io.vavr.PartialFunction
import jakarta.inject.Named
import pl.writeonly.omnibus.rule.Rule
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context

import java.util.List as LegacyList

import io.vavr.collection.List

@Named
class Polonez(private val ruleList: LegacyList<Rule<Context, Bid>>): Function1<Context, Bid> {
    override fun apply(context: Context): Bid {
        val rules: List<PartialFunction<Context, Bid>> = List.ofAll(ruleList)
        return Rule.find(rules, context).getOrElse(Bid.Pass)
    }
}