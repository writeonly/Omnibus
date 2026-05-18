package pl.writeonly.omnibus.rule.domain

import pl.writeonly.omnibus.rule.rules.CandidateBid

data class RecommendationResponse(
    val system: String,
    val evaluatedSeat: String,
    val northHand: String,
    val southHand: String,
    val auction: String,
    val recommendedBid: String,
    val explanation: String,
    val candidates: List<CandidateBid>,
)

