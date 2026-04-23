package com.omnibus.bidding.events;

import java.time.Instant;

public record RuleUpdatedEvent(
    String eventId,
    Instant occurredAt,
    String ruleName,
    String sourcePath,
    boolean managed,
    int contentLength
) {
}
