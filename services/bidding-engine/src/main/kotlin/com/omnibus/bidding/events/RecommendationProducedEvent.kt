package com.omnibus.bidding.events

import java.time.Instant

data class RecommendationProducedEvent(
    val eventId: String,
    val occurredAt: Instant,
    val system: String,
    val evaluatedSeat: String,
    val northHand: String,
    val southHand: String,
    val auction: String,
    val recommendedBid: String,
    val explanation: String,
    val candidateBids: List<String>,
)

