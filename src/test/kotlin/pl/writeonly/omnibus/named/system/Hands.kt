package pl.writeonly.omnibus.named.system

import io.vavr.collection.List

object Hands {

    fun fromString(handString: String): Hand = run {
        val suits = List.ofAll(handString.split(" "))
        require(suits.length() == 4) { "Invalid hand format. Expected four suit groups separated by '.'" }

        val ranksBySuit = suits.map { suitString ->
            List.ofAll(suitString.mapNotNull { char -> Rangs.fromChar(char) })
        }

        Hand(ranksBySuit[0], ranksBySuit[1], ranksBySuit[2], ranksBySuit[3])
    }

}
