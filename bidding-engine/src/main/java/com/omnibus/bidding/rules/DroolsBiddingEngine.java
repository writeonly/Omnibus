package com.omnibus.bidding.rules;

import java.util.ArrayList;
import java.util.List;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class DroolsBiddingEngine {

    private final KieContainer kieContainer;

    public DroolsBiddingEngine(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public List<CandidateBid> evaluate(BiddingFacts biddingFacts) {
        KieSession kieSession = kieContainer.newKieSession();
        List<CandidateBid> candidates = new ArrayList<>();

        try {
            kieSession.insert(biddingFacts);
            kieSession.fireAllRules();

            kieSession.getObjects(object -> object instanceof CandidateBid)
                .forEach(object -> candidates.add((CandidateBid) object));

            return candidates.stream()
                .distinct()
                .sorted((left, right) -> Integer.compare(right.priority(), left.priority()))
                .toList();
        } finally {
            kieSession.dispose();
        }
    }
}
