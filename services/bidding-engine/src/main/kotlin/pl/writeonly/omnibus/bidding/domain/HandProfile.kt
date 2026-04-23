package pl.writeonly.omnibus.bidding.domain

data class HandProfile(
    val normalizedHand: String,
    val spades: Int,
    val hearts: Int,
    val diamonds: Int,
    val clubs: Int,
    val hcp: Int,
    val balanced: Boolean,
)

