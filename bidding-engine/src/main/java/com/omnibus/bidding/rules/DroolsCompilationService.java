package com.omnibus.bidding.rules;

import com.omnibus.bidding.domain.ManagedRuleDefinition;
import java.util.List;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.springframework.stereotype.Service;

@Service
public class DroolsCompilationService {

    public KieContainer buildContainer(List<ManagedRuleDefinition> ruleDefinitions) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        for (ManagedRuleDefinition ruleDefinition : ruleDefinitions) {
            kieFileSystem.write(ruleDefinition.sourcePath(), ruleDefinition.content());
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        boolean hasErrors = kieBuilder.getResults().hasMessages(Message.Level.ERROR);
        if (hasErrors) {
            throw new IllegalArgumentException(kieBuilder.getResults().toString());
        }

        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }
}
