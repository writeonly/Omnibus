package com.omnibus.archive.model;

import java.time.Instant;
import java.util.List;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("recommendations_by_day")
public class RecommendationByDay {

    @PrimaryKey
    private RecommendationByDayKey key;

    private Instant occurredAt;
    private String systemName;
    private String evaluatedSeat;
    private String northHand;
    private String southHand;
    private String auction;
    private String recommendedBid;
    private String explanation;
    private List<String> candidateBids;

    public RecommendationByDay() {
    }

    public RecommendationByDay(
        RecommendationByDayKey key,
        Instant occurredAt,
        String systemName,
        String evaluatedSeat,
        String northHand,
        String southHand,
        String auction,
        String recommendedBid,
        String explanation,
        List<String> candidateBids
    ) {
        this.key = key;
        this.occurredAt = occurredAt;
        this.systemName = systemName;
        this.evaluatedSeat = evaluatedSeat;
        this.northHand = northHand;
        this.southHand = southHand;
        this.auction = auction;
        this.recommendedBid = recommendedBid;
        this.explanation = explanation;
        this.candidateBids = candidateBids;
    }

    public RecommendationByDayKey getKey() {
        return key;
    }
}
