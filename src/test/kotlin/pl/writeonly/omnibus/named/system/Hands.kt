package pl.writeonly.omnibus.named.system

import io.vavr.Tuple
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map

object Hands {

    private val rankMap: Map<Char, Rank> = HashMap.ofEntries(
        Tuple.of('2', Rank.TWO),
        Tuple.of('3', Rank.THREE),
        Tuple.of('4', Rank.FOUR),
        Tuple.of('5', Rank.FIVE),
        Tuple.of('6', Rank.SIX),
        Tuple.of('7', Rank.SEVEN),
        Tuple.of('8', Rank.EIGHT),
        Tuple.of('9', Rank.NINE),
        Tuple.of('T', Rank.TEN),
        Tuple.of('J', Rank.JACK),
        Tuple.of('Q', Rank.QUEEN),
        Tuple.of('K', Rank.KING),
        Tuple.of('A', Rank.ACE)
    )

    fun fromString(handString: String): Hand = run {
        val suits = List.ofAll(handString.split(" "))
        require(suits.length() == 4) { "Invalid hand format. Expected four suit groups separated by '.'" }

        val ranksBySuit = suits.map { suitString ->
            List.ofAll(suitString.mapNotNull { char -> fromChar(char) })
        }

        Hand(ranksBySuit[0], ranksBySuit[1], ranksBySuit[2], ranksBySuit[3])
    }

    private fun fromChar(char: Char): Rank? = rankMap.get(char.uppercaseChar()).getOrNull()
}
