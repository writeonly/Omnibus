package com.omnibus.bidding.application;

import com.omnibus.bidding.domain.HandParser;
import com.omnibus.bidding.domain.HandProfile;
import com.omnibus.bidding.domain.RecommendationRequest;
import com.omnibus.bidding.domain.RecommendationResponse;
import com.omnibus.bidding.rules.BiddingFacts;
import com.omnibus.bidding.rules.CandidateBid;
import com.omnibus.bidding.rules.DroolsBiddingEngine;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BiddingRecommendationService {

    private final HandParser handParser;
    private final DroolsBiddingEngine droolsBiddingEngine;

    public BiddingRecommendationService(HandParser handParser, DroolsBiddingEngine droolsBiddingEngine) {
        this.handParser = handParser;
        this.droolsBiddingEngine = droolsBiddingEngine;
    }

    public RecommendationResponse recommend(RecommendationRequest request) {
        HandProfile handProfile = handParser.parse(request.hand());
        BiddingFacts biddingFacts = BiddingFacts.from(handProfile, request.auction(), request.system());
        List<CandidateBid> candidates = droolsBiddingEngine.evaluate(biddingFacts);
        CandidateBid bestCandidate = candidates.stream()
            .max(Comparator.comparingInt(CandidateBid::priority))
            .orElseGet(() -> new CandidateBid("PASS", 0, "No matching rule found"));

        return new RecommendationResponse(
            request.system(),
            handProfile.normalizedHand(),
            request.auction(),
            bestCandidate.bid(),
            bestCandidate.reason(),
            candidates
        );
    }
}
