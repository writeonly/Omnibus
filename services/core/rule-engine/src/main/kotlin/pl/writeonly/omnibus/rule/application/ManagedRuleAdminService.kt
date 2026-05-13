package pl.writeonly.omnibus.rule.application

import pl.writeonly.omnibus.rule.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.rule.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.rule.events.DomainEventPublisher
import pl.writeonly.omnibus.rule.events.RuleUpdatedEvent
import pl.writeonly.omnibus.rule.rules.DroolsCompilationService
import pl.writeonly.omnibus.rule.rules.RuleCatalogService
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
