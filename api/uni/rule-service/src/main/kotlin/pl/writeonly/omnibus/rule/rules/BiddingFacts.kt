package pl.writeonly.omnibus.rule.rules

import pl.writeonly.omnibus.rule.domain.HandProfile

data class BiddingFacts(
    val system: String,
    val auction: String,
    val hand: String,
    val hcp: Int,
    val spades: Int,
    val hearts: Int,
    val diamonds: Int,
    val clubs: Int,
    val balanced: Boolean,
) {
    companion object {
        fun from(handProfile: HandProfile, auction: String?, system: String): BiddingFacts =
            BiddingFacts(
                system = system,
                auction = auction ?: "",
                hand = handProfile.normalizedHand,
                hcp = handProfile.hcp,
                spades = handProfile.spades,
                hearts = handProfile.hearts,
                diamonds = handProfile.diamonds,
                clubs = handProfile.clubs,
                balanced = handProfile.balanced,
            )
    }
}

