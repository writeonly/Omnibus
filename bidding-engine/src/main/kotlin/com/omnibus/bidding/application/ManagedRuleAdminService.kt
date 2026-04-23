package com.omnibus.bidding.application

import com.omnibus.bidding.domain.ManagedRuleDefinition
import com.omnibus.bidding.domain.ManagedRuleUpsertRequest
import com.omnibus.bidding.events.DomainEventPublisher
import com.omnibus.bidding.events.RuleUpdatedEvent
import com.omnibus.bidding.rules.DroolsCompilationService
import com.omnibus.bidding.rules.RuleCatalogService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant
import java.util.UUID

@Service
class ManagedRuleAdminService(
    private val managedRuleStore: ManagedRuleStore,
    private val ruleCatalogService: RuleCatalogService,
    private val droolsCompilationService: DroolsCompilationService,
    private val domainEventPublisher: DomainEventPublisher,
) {
    fun listRules(): Flux<ManagedRuleDefinition> =
        Mono.fromCallable { ruleCatalogService.listAllRules() }
            .flatMapMany { Flux.fromIterable(it) }
            .subscribeOn(Schedulers.boundedElastic())

    fun saveRule(request: ManagedRuleUpsertRequest): Mono<ManagedRuleDefinition> =
        Mono.fromCallable {
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
                storedRule
            } catch (exception: RuntimeException) {
                managedRuleStore.delete(storedRule.name)
                throw exception
            }
        }.subscribeOn(Schedulers.boundedElastic())
}

