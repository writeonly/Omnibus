package com.omnibus.archive.model

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("recommendations_by_day")
data class RecommendationByDay(
    @PrimaryKey
    val key: RecommendationByDayKey,
    val occurredAt: Instant,
    val systemName: String,
    val evaluatedSeat: String,
    val northHand: String,
    val southHand: String,
    val auction: String,
    val recommendedBid: String,
    val explanation: String,
    val candidateBids: List<String>,
)

