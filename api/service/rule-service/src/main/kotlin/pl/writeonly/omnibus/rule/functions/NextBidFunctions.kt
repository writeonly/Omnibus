package pl.writeonly.omnibus.rule.functions

import pl.writeonly.omnibus.rule.application.ManagedRuleAdminService
import pl.writeonly.omnibus.rule.application.NextBidService
import pl.writeonly.omnibus.rule.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.rule.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.rule.domain.NextBidRequest
import pl.writeonly.omnibus.rule.domain.NextBidResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function
import java.util.function.Supplier

@Configuration
class NextBidFunctions {
    @Bean
    fun nextBid(
        nextBidService: NextBidService,
    ): Function<NextBidRequest, NextBidResponse> =
        Function { request -> nextBidService.nextBid(request) }

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
