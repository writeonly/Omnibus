package pl.writeonly.omnibus.named.system

import io.vavr.collection.List
import io.vavr.collection.Map

object Hands {

    fun fromString(handString: String): Hand = run {
        val suits = List.ofAll(handString.split(" "))
        require(suits.length() == 4) { "Invalid hand format. Expected four suit groups separated by '.'" }

        val ranksBySuit = suits.map { suitString ->
            List.ofAll(suitString.mapNotNull { char -> fromChar(char) })
        }

        Hand(ranksBySuit[0], ranksBySuit[1], ranksBySuit[2], ranksBySuit[3])
    }

    private val rankMap: Map<Char, Rank> = HashMap.of(
        '2', Rank.TWO,
        '3', Rank.THREE,
        '4', Rank.FOUR,
        '5', Rank.FIVE,
        '6', Rank.SIX,
        '7', Rank.SEVEN,
        '8', Rank.EIGHT,
        '9', Rank.NINE,
        'T', Rank.TEN,
        'J', Rank.JACK,
        'Q', Rank.QUEEN,
        'K', Rank.KING,
        'A', Rank.ACE
    )

    private fun fromChar(char: Char): Rank? = rankMap.get(char.uppercaseChar()).getOrNull()

}
