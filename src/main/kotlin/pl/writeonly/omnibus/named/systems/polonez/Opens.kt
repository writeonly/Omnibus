package pl.writeonly.omnibus.named.systems.polonez

import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.*
import pl.writeonly.omnibus.rule.Rule

@Named
class Pass(): Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean {
        return context.hand.doublePoints() < 6u
    }
    override fun apply(hand: Context): Bid {
        return Bid.Pass
    }
}

@Named
class One(): Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean {
        val dp = context.hand.doublePoints()
        return dp >= 6u && dp < 11u
    }
    override fun apply(context: Context): Bid {
        return Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.CLUBS))
    }
}

@Named
class OneNT(): Rule<Context, Bid> {
    override fun isDefinedAt(context: Context): Boolean {
        val dp = context.hand.doublePoints()
        return 11u <= dp
    }
    override fun apply(context: Context): Bid {
        return Bid.LevelBid(Level.ONE, Trump.NoTrump)
    }
}

