package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.Function1
import io.vavr.PartialFunction
import io.vavr.collection.List
import io.vavr.collection.Stream
import io.vavr.control.Option
import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.rule.LazyOptionApply
import pl.writeonly.omnibus.rule.LazyUtil
import pl.writeonly.omnibus.rule.LazyUtil.applyWithDefault
import pl.writeonly.omnibus.rule.LiftedRule
import pl.writeonly.omnibus.rule.Rule
import kotlin.collections.List as LegacyList

@Named
class Polonez(private val ruleList: LegacyList<Rule<Context, Bid>>, private val liftedRuleList: LegacyList<LiftedRule<Context, Bid>>) : Function1<Context, Bid> {
    override fun apply(context: Context): Bid = run {
        val rules: List<PartialFunction<Context, Bid>> = List.ofAll(ruleList)
        val liftedRules: List<Function1<Context, Option<Bid>>> = List.ofAll(liftedRuleList)
        val l1: LazyOptionApply<Bid> = { Rule.find(rules, context) }
        val l2: LazyOptionApply<Bid> = { LiftedRule.find(liftedRules, context) }
        Stream.of(l1, l2).applyWithDefault { Bid.Pass }
    }
}
