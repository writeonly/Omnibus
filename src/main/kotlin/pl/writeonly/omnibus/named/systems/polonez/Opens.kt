package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.collection.Seq
import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Level
import pl.writeonly.omnibus.named.system.Suit
import pl.writeonly.omnibus.named.system.SuitLength
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
class OneSuit : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean = run {
        val dp = context.hand.doublePoints()
        dp >= 6u && dp < 11u
    }
    override fun apply(context: Context): Bid = run {
        val sorted = context.hand.sortedSuitLengths()
        if (4u < sorted.get(0).length) {
            Bid.LevelBid(Level.ONE, Trump.SuitTrump(sorted.get(0).suit))
        } else {
            balanced4(sorted)
        }
    }
    private fun balanced4(sorted: Seq<SuitLength>): Bid = run {
        val minors = sorted.filter { it.suit.isMinor() }
        val minors4 = minors.filter { it.length == 4u }
        when (minors4.size()) {
            2 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.DIAMONDS))
            1 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(minors4.get(0).suit))
            else -> balanced3(minors)
        }
    }
    private fun balanced3(minors: Seq<SuitLength>): Bid = run {
        val minors3 = minors.filter { it.length == 3u }
        when (minors3.size()) {
            2 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.CLUBS))
            1 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(minors3.get(0).suit))
            else -> null!!
        }
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
