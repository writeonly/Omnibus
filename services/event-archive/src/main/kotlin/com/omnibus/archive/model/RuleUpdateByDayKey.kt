package com.omnibus.archive.model

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import java.time.LocalDate
import java.util.UUID

@PrimaryKeyClass
data class RuleUpdateByDayKey(
    @PrimaryKeyColumn(name = "event_date", type = PrimaryKeyType.PARTITIONED)
    val eventDate: LocalDate,
    @PrimaryKeyColumn(name = "event_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    val eventId: UUID,
)

