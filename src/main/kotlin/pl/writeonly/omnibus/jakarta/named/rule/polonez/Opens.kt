package pl.writeonly.omnibus.jakarta.named.rule.polonez

import io.vavr.collection.Seq
import jakarta.inject.Named
import pl.writeonly.omnibus.jakarta.common.rule.Rule
import pl.writeonly.omnibus.jakarta.common.system.Bid
import pl.writeonly.omnibus.jakarta.common.system.Context
import pl.writeonly.omnibus.jakarta.common.system.Level
import pl.writeonly.omnibus.jakarta.common.system.Suit
import pl.writeonly.omnibus.jakarta.common.system.SuitLength
import pl.writeonly.omnibus.jakarta.common.system.Trump

@Named
class Pass : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean = context.hand.doublePoints() < 6u
    override fun apply(hand: Context): Bid = Bid.Pass
}

@Named
class OneSuit : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean =
        context.bidding.trim().isEmpty() && context.hand.doublePoints() in 6u..10u
    override fun apply(context: Context): Bid = run {
        val sorted = context.hand.sortedSuitLengths()
        val theLongest = sorted.head()
        when (theLongest.length) {
            5u -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(theLongest.suit))
            else -> balancedMinor4(sorted)
        }
    }
    private fun balancedMinor4(sorted: Seq<SuitLength>): Bid = run {
        val minors = sorted.filter { it.suit.isMinor() }
        val minors4 = minors.filter { it.length == 4u }
        when (minors4.length()) {
            2 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.DIAMONDS))
            1 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(minors4.head().suit))
            else -> balancedMinor3(minors)
        }
    }
    private fun balancedMinor3(minors: Seq<SuitLength>): Bid = run {
        val minors3 = minors.filter { it.length == 3u }
        when (minors3.length()) {
            2 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.CLUBS))
            1 -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(minors3.head().suit))
            else -> null!!
        }
    }
}

@Named
class OneNT : Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean =
        context.bidding.trim().isEmpty() && context.hand.doublePoints() >= 11u
    override fun apply(context: Context): Bid = Bid.LevelBid(Level.ONE, Trump.NoTrump)
}
