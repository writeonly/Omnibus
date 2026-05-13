package pl.writeonly.omnibus.rule.domain

import org.springframework.stereotype.Component

@Component
class HandParser {
    fun parse(rawHand: String?): HandProfile {
        val normalized = rawHand?.trim()?.uppercase().orEmpty()
        val suits = normalized.split(Regex("\\s+")).filter { it.isNotEmpty() }.toTypedArray()

        if (suits.size != 4) {
            throw IllegalArgumentException("Hand must contain four suit groups separated by spaces")
        }

        val spades = suits[0].length
        val hearts = suits[1].length
        val diamonds = suits[2].length
        val clubs = suits[3].length
        val cardCount = suits.sumOf { it.length }

        if (cardCount != 13) {
            throw IllegalArgumentException("Hand must contain exactly 13 cards")
        }

        val hcp = normalized.sumOf { hcpFor(it) }
        val balanced = isBalanced(spades, hearts, diamonds, clubs)
        return HandProfile(normalized, spades, hearts, diamonds, clubs, hcp, balanced)
    }

    private fun hcpFor(rank: Char): Int =
        when (rank.uppercaseChar()) {
            'A' -> 4
            'K' -> 3
            'Q' -> 2
            'J' -> 1
            else -> 0
        }

    private fun isBalanced(spades: Int, hearts: Int, diamonds: Int, clubs: Int): Boolean {
        val distribution = intArrayOf(spades, hearts, diamonds, clubs)
        var doubletons = 0

        for (suitLength in distribution) {
            if (suitLength < 2 || suitLength > 5) return false
            if (suitLength == 2) doubletons++
        }

        return doubletons <= 1
    }
}

