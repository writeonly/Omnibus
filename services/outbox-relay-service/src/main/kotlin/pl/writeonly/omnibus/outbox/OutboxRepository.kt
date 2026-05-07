import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class OutboxRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun findUnpublished(limit: Int): List<OutboxEvent> {
        return jdbcTemplate.query(
            """
            SELECT id, event_type, aggregate_id, payload
            FROM outbox_events
            WHERE published_at IS NULL
            ORDER BY created_at
            LIMIT ?
            FOR UPDATE SKIP LOCKED
            """.trimIndent(),
            { rs, _ ->
                OutboxEvent(
                    id = rs.getString("id"),
                    eventType = rs.getString("event_type"),
                    aggregateId = rs.getString("aggregate_id"),
                    payload = rs.getString("payload")
                )
            },
            limit
        )
    }

    fun markPublished(id: String) {
        jdbcTemplate.update(
            """
            UPDATE outbox_events
            SET published_at = NOW()
            WHERE id = ?
            """.trimIndent(),
            id
        )
    }

    fun markFailed(id: String, error: String) {
        jdbcTemplate.update(
            """
            UPDATE outbox_events
            SET publish_attempts = publish_attempts + 1,
                last_publish_error = ?
            WHERE id = ?
            """.trimIndent(),
            error,
            id
        )
    }
}
