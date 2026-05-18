package pl.writeonly.omnibus.archive.events

import java.time.Instant

data class BffHttpOutboxEvent(
    val eventId: String,
    val eventType: String,
    val aggregateType: String,
    val aggregateId: String,
    val occurredAt: Instant,
    val method: String,
    val path: String,
    val statusCode: Int,
    val durationMs: Long,
    val request: Map<String, Any?>,
    val response: Any? = null,
    val error: Map<String, Any?>? = null,
)
