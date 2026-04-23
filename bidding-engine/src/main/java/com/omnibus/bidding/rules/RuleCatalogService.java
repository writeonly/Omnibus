package com.omnibus.bidding.rules;

import com.omnibus.bidding.application.ManagedRuleStore;
import com.omnibus.bidding.domain.ManagedRuleDefinition;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class RuleCatalogService {

    private final ManagedRuleStore managedRuleStore;
    private final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public RuleCatalogService(ManagedRuleStore managedRuleStore) {
        this.managedRuleStore = managedRuleStore;
    }

    public List<ManagedRuleDefinition> listAllRules() {
        List<ManagedRuleDefinition> ruleDefinitions = new ArrayList<>();
        ruleDefinitions.addAll(loadBundledRules());
        ruleDefinitions.addAll(managedRuleStore.listManagedRules());
        return ruleDefinitions;
    }

    public List<ManagedRuleDefinition> listManagedRules() {
        return managedRuleStore.listManagedRules();
    }

    private List<ManagedRuleDefinition> loadBundledRules() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath*:rules/*.drl");
            List<ManagedRuleDefinition> bundledRules = new ArrayList<>();

            for (Resource resource : resources) {
                bundledRules.add(new ManagedRuleDefinition(
                    resource.getFilename(),
                    "src/main/resources/rules/" + resource.getFilename(),
                    false,
                    new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                ));
            }

            return bundledRules;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read bundled rules", exception);
        }
    }
}
