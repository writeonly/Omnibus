package com.omnibus.bidding.rules;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfiguration {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource rulesResource = ResourceFactory.newClassPathResource("rules/opening-rules.drl");
        rulesResource.setResourceType(ResourceType.DRL);
        rulesResource.setSourcePath("src/main/resources/rules/opening-rules.drl");
        kieFileSystem.write(rulesResource);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        boolean hasErrors = kieBuilder.getResults().hasMessages(Message.Level.ERROR);
        if (hasErrors) {
            throw new IllegalStateException(kieBuilder.getResults().toString());
        }

        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }
}
