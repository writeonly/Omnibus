package com.omnibus.bidding.domain

import com.omnibus.bidding.rules.CandidateBid

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

