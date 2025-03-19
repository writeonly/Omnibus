package pl.writeonly.omnibus.named.system

import io.vavr.collection.List

object Osika {
    fun countQuarters(hand: Hand): UInt = run {
        val quarters = countHonorQuarters(hand)
        val lengthBonus = hand.suits().values().map { lengthToQuarters(it.size().toUInt()) }.sumOf { it }
        quarters + lengthBonus
    }

    fun countHonorQuarters(hand: Hand): UInt = run {
        val quarters: UInt = hand.cards.sumOf { rankToQuarters(it.rank) }
        val seqBonus = hand.suits().values().map { calculateSeqBonus(it) }.sumOf { it }
        quarters + seqBonus
    }

    private fun rankToQuarters(rank: Rank): UInt = when (rank) {
        Rank.ACE -> 4u
        Rank.KING -> 3u
        Rank.QUEEN -> 2u
        Rank.JACK -> 0u
        else -> 0u
    }

    private fun calculateSeqBonus(cards: List<Card>): UInt = run {
        val honorCount = cards.count { it.rank in listOf(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK) }
        when (honorCount.toUInt()) {
            2u -> 2u
            3u -> 3u
            else -> 0u
        }
    }

    private fun lengthToQuarters(l: UInt): UInt = when (l) {
        4u -> 2u
        5u -> 6u
        6u -> 11u
        7u -> 17u
        8u -> 22u
        else -> if (8u < l) lengthToQuarters(8u) else 0u
    }
}
