package pl.writeonly.omnibus.named.system

import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.collection.Seq

class Hand {
    val cards: List<Card>

    constructor(cards: List<Card>) {
        require(
            cards.length() == Companion.N
        ) { "A bridge hand must contain exactly 13 cards. %d".format(cards.length()) }
        this.cards = cards
    }

    constructor(spades: List<Rank>, hearts: List<Rank>, diamonds: List<Rank>, clubs: List<Rank>) : this(
        spades.map { Card(it, Suit.SPADES) }
            .appendAll(hearts.map { Card(it, Suit.HEARTS) })
            .appendAll(diamonds.map { Card(it, Suit.DIAMONDS) })
            .appendAll(clubs.map { Card(it, Suit.CLUBS) })
    )

    fun doublePoints() = points() / 2u

    fun points(): UInt = cards.sumOf { it.points().toInt() }.toUInt()

    fun suits(): Map<Suit, List<Card>> = cards.groupBy { it.suit }

    override fun toString(): String = cards.joinToString(" ")

    fun sortedSuitLengths(): Seq<SuitLength> = suits()
        .map { t -> SuitLength(t._1, t._2.length().toUInt()) }
        .sorted(
            compareByDescending<SuitLength> { it.length }
                .thenBy { suitOrder[it.suit].getOrElse(Int.MAX_VALUE) }
        )

    companion object {
        const val N = 13

        private val suitOrder = HashMap.of(
            Suit.SPADES,
            0,
            Suit.HEARTS,
            1,
            Suit.DIAMONDS,
            2,
            Suit.CLUBS,
            3
        )
    }
}

data class SuitLength(val suit: Suit, val length: UInt,)

data class Card(val rank: Rank, val suit: Suit) {
    fun points(): UInt = when (rank) {
        Rank.ACE -> 4u
        Rank.KING -> 3u
        Rank.QUEEN -> 2u
        Rank.JACK -> 1u
        else -> 0u
    }
}

enum class Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}
