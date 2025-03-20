package pl.writeonly.omnibus.named.systems.polonez

import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Level
import pl.writeonly.omnibus.named.system.Suit
import pl.writeonly.omnibus.named.system.Trump
import pl.writeonly.omnibus.rule.Rule

@Named
class Pass : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean = run {
        context.hand.doublePoints() < 6u
    }
    override fun apply(hand: Context): Bid = Bid.Pass
}

@Named
class One : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean = run {
        val dp = context.hand.doublePoints()
        dp >= 6u && dp < 11u
    }
    override fun apply(context: Context): Bid = run {
        val sorted = context.hand.sortedSuitLengths()
        Bid.LevelBid(Level.ONE, Trump.SuitTrump(sorted.get(0)._1))
    }


}

@Named
class OneNT : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean = run {
        val dp = context.hand.doublePoints()
        11u <= dp
    }
    override fun apply(context: Context): Bid = Bid.LevelBid(Level.ONE, Trump.NoTrump)
}
