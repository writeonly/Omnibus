package com.omnibus.bidding.rules;

import java.util.ArrayList;
import java.util.List;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class DroolsBiddingEngine {

    private final RuleCatalogService ruleCatalogService;
    private final DroolsCompilationService droolsCompilationService;

    public DroolsBiddingEngine(
        RuleCatalogService ruleCatalogService,
        DroolsCompilationService droolsCompilationService
    ) {
        this.ruleCatalogService = ruleCatalogService;
        this.droolsCompilationService = droolsCompilationService;
    }

    public List<CandidateBid> evaluate(BiddingFacts biddingFacts) {
        KieSession kieSession = droolsCompilationService
            .buildContainer(ruleCatalogService.listAllRules())
            .newKieSession();
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
