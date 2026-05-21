package pl.writeonly.omnibus.rule.functions

import pl.writeonly.omnibus.rule.application.ManagedRuleAdminService
import pl.writeonly.omnibus.rule.application.RestBiddingService
import pl.writeonly.omnibus.rule.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.rule.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.rule.domain.RecommendationRequest
import pl.writeonly.omnibus.rule.domain.RecommendationResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function
import java.util.function.Supplier

@Configuration
class BiddingFunctions {
    @Bean
    fun recommendBid(
        biddingRecommendationService: RestBiddingService,
    ): Function<RecommendationRequest, RecommendationResponse> =
        Function { request -> biddingRecommendationService.recommend(request) }

    @Bean
    fun listManagedRules(
        managedRuleAdminService: ManagedRuleAdminService,
    ): Supplier<List<ManagedRuleDefinition>> =
        Supplier { managedRuleAdminService.listRules() }

    @Bean
    fun saveManagedRule(
        managedRuleAdminService: ManagedRuleAdminService,
    ): Function<ManagedRuleUpsertRequest, ManagedRuleDefinition> =
        Function { request -> managedRuleAdminService.saveRule(request) }
}
