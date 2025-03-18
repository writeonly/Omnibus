package pl.writeonly.omnibus.named.system

import io.vavr.collection.List
import io.vavr.collection.Map

class Hand {
    private val cards: List<Card>

    constructor(cards: List<Card>) {
        require(cards.length() == 13) { "A bridge hand must contain exactly 13 cards. %d".format(cards.length()) }
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

    fun isBalanced(): Boolean {
        val suitCounts = suits().values().map { it.size() }
        return suitCounts.contains(3) || suitCounts.all { it in 2..5 }
    }

    override fun toString(): String = cards.joinToString(" ")
}

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
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
}

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}
