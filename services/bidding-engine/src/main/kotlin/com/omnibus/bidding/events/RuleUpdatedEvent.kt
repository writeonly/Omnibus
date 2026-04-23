package com.omnibus.bidding.events

import java.time.Instant

data class RuleUpdatedEvent(
    val eventId: String,
    val occurredAt: Instant,
    val ruleName: String,
    val sourcePath: String,
    val managed: Boolean,
    val contentLength: Int,
)

