package com.omnibus.bidding.rules

import org.kie.api.runtime.KieSession
import org.springframework.stereotype.Component

@Component
class DroolsBiddingEngine(
    private val ruleCatalogService: RuleCatalogService,
    private val droolsCompilationService: DroolsCompilationService,
) {
    fun evaluate(biddingFacts: BiddingFacts): List<CandidateBid> {
        val kieSession: KieSession =
            droolsCompilationService
                .buildContainer(ruleCatalogService.listAllRules())
                .newKieSession()

        try {
            kieSession.insert(biddingFacts)
            kieSession.fireAllRules()

            val candidates: List<CandidateBid> =
                kieSession.getObjects { it is CandidateBid }
                    .mapNotNull { it as? CandidateBid }

            return candidates
                .distinct()
                .sortedByDescending { it.priority }
        } finally {
            kieSession.dispose()
        }
    }
}

