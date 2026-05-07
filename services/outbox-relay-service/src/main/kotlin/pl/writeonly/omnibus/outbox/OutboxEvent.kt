data class OutboxEvent(
    val id: String,
    val eventType: String,
    val aggregateId: String,
    val payload: String
)
