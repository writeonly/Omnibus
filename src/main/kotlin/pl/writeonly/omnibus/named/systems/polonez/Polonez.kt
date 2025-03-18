package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.Function1
import io.vavr.PartialFunction
import io.vavr.collection.List
import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.rule.Rule
import kotlin.collections.List as LegacyList

@Named
class Polonez(private val ruleList: LegacyList<Rule<Context, Bid>>) : Function1<Context, Bid> {
    override fun apply(context: Context): Bid = run {
        val rules: List<PartialFunction<Context, Bid>> = List.ofAll(ruleList)
        Rule.find(rules, context).getOrElse(Bid.Pass)
    }
}
