package com.omnibus.bidding.application;

import com.omnibus.bidding.domain.ManagedRuleDefinition;
import com.omnibus.bidding.domain.ManagedRuleUpsertRequest;
import com.omnibus.bidding.events.DomainEventPublisher;
import com.omnibus.bidding.events.RuleUpdatedEvent;
import com.omnibus.bidding.rules.DroolsCompilationService;
import com.omnibus.bidding.rules.RuleCatalogService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ManagedRuleAdminService {

    private final ManagedRuleStore managedRuleStore;
    private final RuleCatalogService ruleCatalogService;
    private final DroolsCompilationService droolsCompilationService;
    private final DomainEventPublisher domainEventPublisher;

    public ManagedRuleAdminService(
        ManagedRuleStore managedRuleStore,
        RuleCatalogService ruleCatalogService,
        DroolsCompilationService droolsCompilationService,
        DomainEventPublisher domainEventPublisher
    ) {
        this.managedRuleStore = managedRuleStore;
        this.ruleCatalogService = ruleCatalogService;
        this.droolsCompilationService = droolsCompilationService;
        this.domainEventPublisher = domainEventPublisher;
    }

    public List<ManagedRuleDefinition> listRules() {
        return ruleCatalogService.listAllRules();
    }

    public ManagedRuleDefinition saveRule(ManagedRuleUpsertRequest request) {
        ManagedRuleDefinition storedRule = managedRuleStore.save(request.name(), request.content());

        try {
            droolsCompilationService.buildContainer(ruleCatalogService.listAllRules());
            domainEventPublisher.publishRuleUpdated(new RuleUpdatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                storedRule.name(),
                storedRule.sourcePath(),
                storedRule.managed(),
                storedRule.content().length()
            ));
            return storedRule;
        } catch (RuntimeException exception) {
            managedRuleStore.delete(storedRule.name());
            throw exception;
        }
    }
}
