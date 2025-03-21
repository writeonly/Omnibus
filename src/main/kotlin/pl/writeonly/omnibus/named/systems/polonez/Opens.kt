package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.collection.Seq
import io.vavr.control.Option
import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Level
import pl.writeonly.omnibus.named.system.Suit
import pl.writeonly.omnibus.named.system.SuitLength
import pl.writeonly.omnibus.named.system.Trump
import pl.writeonly.omnibus.rule.LiftedRule
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

@Named
class OverMinorOne : LiftedRule<Context, Bid> {
    override fun apply(context: Context): Option<Bid> = run {
        val bidding = context.bidding.trim().raw
        val head = bidding.headOption()

        when (val opening = head.getOrNull()) {
            is Bid.LevelBid -> when {
                opening.level == Level.ONE && opening.trump is Trump.SuitTrump && opening.trump.suit.isMinor() ->
                    apply(context, opening.trump.suit)
                else -> Option.none()
            }
            else -> Option.none()
        }
    }

    fun apply(context: Context, openingSuit: Suit): Option<Bid> = run {
        val hand = context.hand
        val points = hand.doublePoints()
        val suits = hand.sortedSuitLengths()
        val longest = suits.head()
        val suit4 = suits.filter { it.length == 4u }

        when {
            points < 3u -> Option.of(Bid.Pass)
            longest.suit.isMajor() && longest.length >= 5u -> Option.of(
                Bid.LevelBid(Level.ONE, Trump.SuitTrump(longest.suit))
            )
            points in 3u..4u -> Option.of(weak(context, openingSuit, suit4))
            else -> Option.none()
        }
    }

    private fun weak(context: Context, openingSuit: Suit, suit4: Seq<SuitLength>): Bid =
        oneOverOne(openingSuit, suit4).orElse(three(context, openingSuit)).getOrElse(
            Bid.LevelBid(Level.ONE, Trump.NoTrump)
        )

    private fun oneOverOne(openingSuit: Suit, suit4: Seq<SuitLength>): Option<Bid> =
        suit4.filter { it.suit.isOldest(openingSuit) }.lastOption().map {
            Bid.LevelBid(Level.ONE, Trump.SuitTrump(it.suit))
        }

    private fun three(context: Context, openingSuit: Suit): Option<Bid> =
        context.hand.suits().get(openingSuit).map { it.length().toUInt() }.filter { it >= 5u }.map {
            Bid.LevelBid(Level.THREE, Trump.SuitTrump(openingSuit))
        }
}
