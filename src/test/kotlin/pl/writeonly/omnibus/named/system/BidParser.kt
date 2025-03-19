package pl.writeonly.omnibus.named.system

import io.vavr.Tuple
import io.vavr.collection.HashMap
import io.vavr.collection.Map

object BidParser {

    private val levelMap: Map<Char, Level> = HashMap.ofEntries(
        Tuple.of('1', Level.ONE),
        Tuple.of('2', Level.TWO),
        Tuple.of('3', Level.THREE),
        Tuple.of('4', Level.FOUR),
        Tuple.of('5', Level.FIVE),
        Tuple.of('6', Level.SIX),
        Tuple.of('7', Level.SEVEN)
    )

    private val trumpMap: Map<String, Trump> = HashMap.ofEntries(
        Tuple.of("C", Trump.SuitTrump(Suit.CLUBS)),
        Tuple.of("D", Trump.SuitTrump(Suit.DIAMONDS)),
        Tuple.of("H", Trump.SuitTrump(Suit.HEARTS)),
        Tuple.of("S", Trump.SuitTrump(Suit.SPADES)),
        Tuple.of("NT", Trump.NoTrump)
    )

    fun parse(bidString: String): Bid? = when (bidString.uppercase()) {
        "PASS" -> Bid.Pass
        "X" -> Bid.Double
        "XX" -> Bid.Redouble
        else -> parseLevelBid(bidString)
    }

    private fun parseLevelBid(bidString: String) = run {
        val level = levelMap.get(bidString[0]).getOrNull()
        val trump = trumpMap.get(bidString.substring(1).uppercase()).getOrNull()
        if (level != null && trump != null) {
            Bid.LevelBid(level, trump)
        } else {
            null
        }
    }
}
