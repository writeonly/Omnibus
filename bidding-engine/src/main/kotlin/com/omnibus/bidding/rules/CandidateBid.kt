package com.omnibus.bidding.rules

data class CandidateBid(
    val bid: String,
    val priority: Int,
    val reason: String,
)

