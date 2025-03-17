package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.Function1
import jakarta.inject.Named
import pl.writeonly.omnibus.rule.Rule
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context

import io.vavr.collection.List as VavrList

@Named
class Polonez(val ruleList: List<Rule<Context, Bid>>): Function1<Context, Bid> {
    override fun apply(context: Context): Bid {
        val rules: VavrList<Rule<Context, Bid>> = VavrList.ofAll(ruleList)
        return rules.find { it.isDefinedAt(context) }.map { it.apply(context) }.getOrElse(Bid.Pass)
    }
}