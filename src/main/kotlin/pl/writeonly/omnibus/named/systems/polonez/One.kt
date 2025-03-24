package pl.writeonly.omnibus.named.systems.polonez

import io.vavr.collection.Seq
import io.vavr.collection.Stream
import io.vavr.control.Option
import jakarta.inject.Named
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Level
import pl.writeonly.omnibus.named.system.Suit
import pl.writeonly.omnibus.named.system.SuitLength
import pl.writeonly.omnibus.named.system.Trump
import pl.writeonly.omnibus.rule.LazyUtil.applyWithDefault
import pl.writeonly.omnibus.rule.LiftedRule


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
        Stream.of(
            { oneOverOne(openingSuit, suit4) },
            { three(context, openingSuit) },
        )
            .applyWithDefault { Bid.LevelBid(Level.ONE, Trump.NoTrump) }

    private fun oneOverOne(openingSuit: Suit, suit4: Seq<SuitLength>): Option<Bid> =
        suit4.filter { it.suit.isOldest(openingSuit) }.lastOption().map {
            Bid.LevelBid(Level.ONE, Trump.SuitTrump(it.suit))
        }

    private fun three(context: Context, openingSuit: Suit): Option<Bid> =
        context.hand.suits().get(openingSuit).map { it.length().toUInt() }.filter { it >= 5u }.map {
            Bid.LevelBid(Level.THREE, Trump.SuitTrump(openingSuit))
        }
}
