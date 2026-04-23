package com.omnibus.bidding.application;

import com.omnibus.bidding.domain.ManagedRuleDefinition;
import com.omnibus.bidding.domain.ManagedRuleUpsertRequest;
import com.omnibus.bidding.rules.DroolsCompilationService;
import com.omnibus.bidding.rules.RuleCatalogService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ManagedRuleAdminService {

    private final ManagedRuleStore managedRuleStore;
    private final RuleCatalogService ruleCatalogService;
    private final DroolsCompilationService droolsCompilationService;

    public ManagedRuleAdminService(
        ManagedRuleStore managedRuleStore,
        RuleCatalogService ruleCatalogService,
        DroolsCompilationService droolsCompilationService
    ) {
        this.managedRuleStore = managedRuleStore;
        this.ruleCatalogService = ruleCatalogService;
        this.droolsCompilationService = droolsCompilationService;
    }

    public List<ManagedRuleDefinition> listRules() {
        return ruleCatalogService.listAllRules();
    }

    public ManagedRuleDefinition saveRule(ManagedRuleUpsertRequest request) {
        ManagedRuleDefinition storedRule = managedRuleStore.save(request.name(), request.content());

        try {
            droolsCompilationService.buildContainer(ruleCatalogService.listAllRules());
            return storedRule;
        } catch (RuntimeException exception) {
            managedRuleStore.delete(storedRule.name());
            throw exception;
        }
    }
}
