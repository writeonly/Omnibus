package com.omnibus.bidding.domain;

import com.omnibus.bidding.rules.CandidateBid;
import java.util.List;

public record RecommendationResponse(
    String system,
    String evaluatedSeat,
    String northHand,
    String southHand,
    String auction,
    String recommendedBid,
    String explanation,
    List<CandidateBid> candidates
) {
}
