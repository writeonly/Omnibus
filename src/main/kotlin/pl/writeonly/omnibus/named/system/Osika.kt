package pl.writeonly.omnibus.named.system

import io.vavr.collection.List

object Osika {
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

    @Suppress("MagicNumber")
    private fun calculateSeqBonus(cards: List<Card>): UInt = run {
        val honorCount = cards.count { it.rank in listOf(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK) }
        when (honorCount) {
            2 -> 2u
            3 -> 3u
            else -> 0u
        }
    }
}
