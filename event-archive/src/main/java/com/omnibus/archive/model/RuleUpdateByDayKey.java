package com.omnibus.archive.model;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class RuleUpdateByDayKey {

    @PrimaryKeyColumn(name = "event_date", type = PrimaryKeyType.PARTITIONED)
    private LocalDate eventDate;

    @PrimaryKeyColumn(name = "event_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    private UUID eventId;

    public RuleUpdateByDayKey() {
    }

    public RuleUpdateByDayKey(LocalDate eventDate, UUID eventId) {
        this.eventDate = eventDate;
        this.eventId = eventId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public UUID getEventId() {
        return eventId;
    }
}
