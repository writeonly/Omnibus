package com.omnibus.bidding.application;

import com.omnibus.bidding.domain.HandParser;
import com.omnibus.bidding.domain.HandProfile;
import com.omnibus.bidding.domain.RecommendationRequest;
import com.omnibus.bidding.domain.RecommendationResponse;
import com.omnibus.bidding.events.DomainEventPublisher;
import com.omnibus.bidding.events.RecommendationProducedEvent;
import com.omnibus.bidding.rules.BiddingFacts;
import com.omnibus.bidding.rules.CandidateBid;
import com.omnibus.bidding.rules.DroolsBiddingEngine;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class BiddingRecommendationService {

    private final HandParser handParser;
    private final DroolsBiddingEngine droolsBiddingEngine;
    private final DomainEventPublisher domainEventPublisher;

    public BiddingRecommendationService(
        HandParser handParser,
        DroolsBiddingEngine droolsBiddingEngine,
        DomainEventPublisher domainEventPublisher
    ) {
        this.handParser = handParser;
        this.droolsBiddingEngine = droolsBiddingEngine;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Mono<RecommendationResponse> recommend(RecommendationRequest request) {
        return Mono.fromCallable(() -> {
                HandProfile northHandProfile = handParser.parse(request.northHand());
                handParser.parse(request.southHand());
                BiddingFacts biddingFacts = BiddingFacts.from(northHandProfile, request.auction(), request.system());
                List<CandidateBid> candidates = droolsBiddingEngine.evaluate(biddingFacts);
                CandidateBid bestCandidate = candidates.stream()
                    .max(Comparator.comparingInt(CandidateBid::priority))
                    .orElseGet(() -> new CandidateBid("PASS", 0, "No matching rule found"));

                RecommendationResponse response = new RecommendationResponse(
                    request.system(),
                    "NORTH",
                    northHandProfile.normalizedHand(),
                    request.southHand().trim().toUpperCase(),
                    request.auction(),
                    bestCandidate.bid(),
                    "%s Current MVP evaluates the opening decision for North.".formatted(bestCandidate.reason()),
                    candidates
                );

                domainEventPublisher.publishRecommendationProduced(new RecommendationProducedEvent(
                    UUID.randomUUID().toString(),
                    Instant.now(),
                    response.system(),
                    response.evaluatedSeat(),
                    response.northHand(),
                    response.southHand(),
                    response.auction(),
                    response.recommendedBid(),
                    response.explanation(),
                    response.candidates().stream().map(CandidateBid::bid).toList()
                ));

                return response;
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
