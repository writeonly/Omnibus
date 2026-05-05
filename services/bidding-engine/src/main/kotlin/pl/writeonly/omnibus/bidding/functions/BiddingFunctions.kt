package pl.writeonly.omnibus.bidding.functions

import pl.writeonly.omnibus.bidding.application.ManagedRuleAdminService
import pl.writeonly.omnibus.bidding.application.RestBiddingationService
import pl.writeonly.omnibus.bidding.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.bidding.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function
import java.util.function.Supplier

@Configuration
class BiddingFunctions {
    @Bean
    fun recommendBid(
        biddingRecommendationService: RestBiddingationService,
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
