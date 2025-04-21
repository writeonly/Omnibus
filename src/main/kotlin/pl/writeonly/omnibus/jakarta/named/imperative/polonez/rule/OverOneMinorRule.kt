package pl.writeonly.omnibus.jakarta.named.imperative.polonez.rule

import io.vavr.collection.Seq
import io.vavr.collection.Stream
import io.vavr.control.Option
import jakarta.inject.Named
import pl.writeonly.omnibus.jakarta.common.rule.LazyUtil.applyWithDefault
import pl.writeonly.omnibus.jakarta.common.rule.LiftedRule
import pl.writeonly.omnibus.jakarta.common.system.Bid
import pl.writeonly.omnibus.jakarta.common.system.Context
import pl.writeonly.omnibus.jakarta.common.system.Level
import pl.writeonly.omnibus.jakarta.common.system.Suit
import pl.writeonly.omnibus.jakarta.common.system.SuitLength
import pl.writeonly.omnibus.jakarta.common.system.Trump

@Named
class OverOneMinorRule : LiftedRule<Context, Bid> {
    override fun apply(context: Context): Option<Bid> = run {
        val raw = context.bidding.trim().raw
        when (raw.length()) {
            2 -> apply(context, raw.head())
            else -> Option.none()
        }
    }

    fun apply(context: Context, opening: Bid): Option<Bid> = when (opening) {
        is Bid.LevelBid -> apply(context, opening)
        else -> Option.none()
    }

    fun apply(context: Context, opening: Bid.LevelBid): Option<Bid> = when {
        opening.level == Level.ONE && opening.trump is Trump.SuitTrump && opening.trump.suit.isMinor() ->
            Option.of(apply(context, opening.trump.suit))
        else -> Option.none()
    }

    fun apply(context: Context, openingSuit: Suit): Bid = run {
        val hand = context.hand
        val points = hand.doublePoints()
        val suits = hand.sortedSuitLengths()
        val longest = suits.head()
        val suit4 = suits.filter { it.length == 4u }

        when {
            points < 3u -> Bid.Pass
            longest.suit.isMajor() && longest.length >= 5u -> Bid.LevelBid(Level.ONE, Trump.SuitTrump(longest.suit))
            points in 3u..4u -> weak(context, openingSuit, suit4)
            points == 5u -> Bid.LevelBid(Level.TWO, Trump.NoTrump) // TODO
            points >= 6u -> Bid.LevelBid(Level.THREE, Trump.NoTrump) // TODO
            else -> Bid.LevelBid(Level.THREE, Trump.NoTrump) // TODO
        }
    }

    fun pass(points: UInt): Option<Bid> =
        Option.`when`(points < 3u) { Bid.Pass }

    private fun weak(context: Context, openingSuit: Suit, suit4: Seq<SuitLength>): Bid =
        Stream.of(
            { oneOverOne(openingSuit, suit4) },
            { threeInOpeningSuit(context, openingSuit) },
        )
            .applyWithDefault { Bid.LevelBid(Level.ONE, Trump.NoTrump) }

    private fun oneOverOne(openingSuit: Suit, suit4: Seq<SuitLength>): Option<Bid> =
        suit4.filter { it.suit.isOldest(openingSuit) }.lastOption().map {
            Bid.LevelBid(Level.ONE, Trump.SuitTrump(it.suit))
        }

    private fun threeInOpeningSuit(context: Context, openingSuit: Suit): Option<Bid> =
        context.hand.suits().get(openingSuit).map { it.length().toUInt() }.filter { it >= 5u }.map {
            Bid.LevelBid(Level.THREE, Trump.SuitTrump(openingSuit))
        }
}
