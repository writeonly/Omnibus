package pl.writeonly.omnibus.named.system

import io.vavr.collection.List

object Hands {

    fun fromString(handString: String): Hand {
        val suits = List.ofAll(handString.split(" "))
        require(suits.length() == 4) { "Invalid hand format. Expected four suit groups separated by '.'" }

        val ranksBySuit = suits.map { suitString ->
            List.ofAll(suitString.mapNotNull { char -> fromChar(char) })
        }

        return Hand(ranksBySuit[0], ranksBySuit[1], ranksBySuit[2], ranksBySuit[3])
    }

    private fun fromChar(char: Char): Rank? = when (char.uppercaseChar()) {
        '2' -> Rank.TWO
        '3' -> Rank.THREE
        '4' -> Rank.FOUR
        '5' -> Rank.FIVE
        '6' -> Rank.SIX
        '7' -> Rank.SEVEN
        '8' -> Rank.EIGHT
        '9' -> Rank.NINE
        'T' -> Rank.TEN
        'J' -> Rank.JACK
        'Q' -> Rank.QUEEN
        'K' -> Rank.KING
        'A' -> Rank.ACE
        else -> null
    }


    val wholeSuit = List.of(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX,  Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO)

    val wholeClubs = Hand(List.of(), List.of(), List.of(), wholeSuit)

    val weakHand = Hand(List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.TWO))

    val strongHand = Hand(List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO), List.of(Rank.TWO))

}