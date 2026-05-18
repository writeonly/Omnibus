package pl.writeonly.omnibus.archive.model

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("bff_http_events_by_day")
data class BffHttpEventByDay(
    @PrimaryKey
    val key: BffHttpEventByDayKey,
    val occurredAt: Instant,
    val eventType: String,
    val aggregateType: String,
    val aggregateId: String,
    val method: String,
    val path: String,
    val statusCode: Int,
    val durationMs: Long,
    val requestJson: String,
    val responseJson: String?,
    val errorJson: String?,
)
