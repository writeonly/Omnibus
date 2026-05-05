package pl.writeonly.omnibus.bidding.application

import pl.writeonly.omnibus.bidding.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.bidding.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.bidding.events.DomainEventPublisher
import pl.writeonly.omnibus.bidding.events.RuleUpdatedEvent
import pl.writeonly.omnibus.bidding.rules.DroolsCompilationService
import pl.writeonly.omnibus.bidding.rules.RuleCatalogService
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ManagedRuleAdminService(
    private val managedRuleStore: ManagedRuleStore,
    private val ruleCatalogService: RuleCatalogService,
    private val droolsCompilationService: DroolsCompilationService,
    private val domainEventPublisher: DomainEventPublisher,
) {
    fun listRules(): List<ManagedRuleDefinition> = ruleCatalogService.listAllRules()

    fun saveRule(request: ManagedRuleUpsertRequest): ManagedRuleDefinition {
        val storedRule = managedRuleStore.save(request.name, request.content)

        try {
            droolsCompilationService.buildContainer(ruleCatalogService.listAllRules())
            domainEventPublisher.publishRuleUpdated(
                RuleUpdatedEvent(
                    UUID.randomUUID().toString(),
                    Instant.now(),
                    storedRule.name,
                    storedRule.sourcePath,
                    storedRule.managed,
                    storedRule.content.length,
                ),
            )
            return storedRule
        } catch (exception: RuntimeException) {
            managedRuleStore.delete(storedRule.name)
            throw exception
        }
    }
}
