package pl.writeonly.omnibus.user.events

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "outbox_events")
class OutboxEvent(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "aggregate_type", nullable = false)
    val aggregateType: String,

    @Column(name = "aggregate_id", nullable = false)
    val aggregateId: String,

    @Column(name = "event_type", nullable = false)
    val eventType: String,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    val payload: Map<String, Any?>,

    @Column(name = "occurred_at", nullable = false)
    val occurredAt: Instant = Instant.now(),
)
