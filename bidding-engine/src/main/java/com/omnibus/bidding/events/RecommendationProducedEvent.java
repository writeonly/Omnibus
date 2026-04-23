package com.omnibus.bidding.events;

import java.time.Instant;
import java.util.List;

public record RecommendationProducedEvent(
    String eventId,
    Instant occurredAt,
    String system,
    String evaluatedSeat,
    String northHand,
    String southHand,
    String auction,
    String recommendedBid,
    String explanation,
    List<String> candidateBids
) {
}
